package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.github.ollgei.base.commonj.utils.CommonHelper;
import com.github.ollgei.spring.boot.autoconfigure.core.OllgeiProperties;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.OllgeiDisruptorProperties;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.spring.SpringOllgeiDisruptorSubscription;
import com.github.ollgei.spring.boot.autoconfigure.serialization.SerializationManager;
import com.github.ollgei.spring.boot.autoconfigure.serialization.SerializationObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractRetryableService<T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse>
        implements RetryableService<T, U, S>, OllgeiDisruptorService<RetryableContext> {

    private OllgeiDisruptorPublisher publisher;

    private RetryableRepository retryableRepository;

    private SerializationManager serializationManager;

    private OllgeiDisruptorProperties ollgeiDisruptorProperties;

    private OllgeiProperties ollgeiProperties;

    @Override
    public void publish(RetryableContext context) {
        Assert.notNull(publisher, "OllgeiDisruptorPublisher not null!");
        final SpringOllgeiDisruptorSubscription subscription = new SpringOllgeiDisruptorSubscription();
        subscription.setContext(context);
        subscription.setClazz(this.getClass());
        publisher.write(subscription);
    }

    @Override
    public void read(RetryableContext context) {
        final RetryableStateEnum rState = readState(context);
        if (rState == RetryableStateEnum.SUCCESS) {
            if (log.isInfoEnabled()) {
                log.info("【异步重试】已经全部执行完成");
            }
            countDown(context);
            return;
        }
        int state = rState.getCode();
        final T uResponse;

        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            uResponse = upstream(context);
            if (uResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
                writeUpstreamResponse(context, uResponse, state);
            } else if (uResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = readUpstreamResponse(context);
        }

        //2 执行中游业务处理 保持幂等性(本地处理)
        final U mResponse;

        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.MIDSTREAM_FAIL)) {
            mResponse = midstream(context, uResponse);
            if (mResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
                writeMidstreamResponse(context, mResponse, state);
            } else if (mResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        } else {
            mResponse = readMidstreamResponse(context);
        }

        //判断第3位是0
        final S dResponse;
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            dResponse = downstream(context, uResponse, mResponse);
            if (dResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
                writeDownstreamResponse(context, dResponse, state);
                return;
            } else if (dResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
        //最后写入state
        writeState(context, state, true);
        countDown(context);
    }

    @Override
    public void write(RetryableContext context) {
        final RetryableModel model = new RetryableModel();
        if (CommonHelper.hasText(context.getAppId())) {
            model.setAppId(context.getAppId());
        } else {
            model.setAppId(ollgeiProperties.getAppId());
        }
        model.setBizKind(context.getBizKind());
        model.setBizId(context.getBizId());
        model.setBizSubNo(context.getBizSubNo().shortValue());
        model.setState(RetryableStateEnum.INIT.getCode());
        model.setRetryCount(0);
        model.setNextRetryTimestamp(0L);
        model.setParams(serializationManager.serializeNativeObject(context.getData()));
        retryableRepository.save(model);
    }

    @Override
    public RetryableStateEnum readState(RetryableContext context) {
        return RetryableStateEnum.resolve(retryableRepository.readState(context));
    }

    @Override
    public void writeState(RetryableContext context, int state, boolean success) {
        final RetryableModel model = new RetryableModel();
        model.setState(state);
        if (!success) {
            model.setNextRetryIncrTimestamp(getNextDelay());
            model.setRetryIncrCount(1);
        }
        retryableRepository.update(context, model);
    }

    @Override
    public void writeResponse(RetryableContext context, T uResponse, U mResponse, S dResponse, int state) {
        final RetryableModel model = new RetryableModel();
        model.setState(state);
        if (Objects.nonNull(uResponse)) {
            model.setUpstreamResponse(serializationManager.serializeObject(
                    SerializationObject.builder().object(uResponse).build()));
        }
        if (Objects.nonNull(mResponse)) {
            model.setUpstreamResponse(serializationManager.serializeObject(
                    SerializationObject.builder().object(mResponse).build()));
        }
        if (Objects.nonNull(dResponse)) {
            model.setDownstreamResponse(serializationManager.serializeObject(
                    SerializationObject.builder().object(dResponse).build()));
        }
        retryableRepository.update(context, model);
    }

    private long getNextDelay() {
        final OllgeiDisruptorProperties.Retryable retryableProps = ollgeiDisruptorProperties.getRetryable();
        return new Double(retryableProps.getDelay() * retryableProps.getMultiplier()).longValue();
    }

    @Override
    public T readUpstreamResponse(RetryableContext context, Class<T> cls) {
        final byte[] response = retryableRepository.readUpstreamResponse(context);
        if (Objects.isNull(response) || response.length == 0) {
            return null;
        }
        return serializationManager.deserializeObject(response, cls);
    }

    @Override
    public U readMidstreamResponse(RetryableContext context, Class<U> cls) {
        final byte[] response = retryableRepository.readMidstreamResponse(context);
        if (Objects.isNull(response) || response.length == 0) {
            return null;
        }
        return serializationManager.deserializeObject(response, cls);
    }

    @Override
    public S readDownstreamResponse(RetryableContext context, Class<S> cls) {
        final byte[] response = retryableRepository.readMidstreamResponse(context);
        if (Objects.isNull(response) || response.length == 0) {
            return null;
        }
        return serializationManager.deserializeObject(response, cls);
    }

    private void countDown(RetryableContext context) {
        if (context.getLatch() != null && context.getLatch().getCount() > 0) {
            context.getLatch().countDown();
        }
    }

    public OllgeiDisruptorPublisher getPublisher() {
        return publisher;
    }

    @Autowired
    public void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired
    public void setRetryableRepository(RetryableRepository retryableRepository) {
        this.retryableRepository = retryableRepository;
    }

    @Autowired
    public void setSerializationManager(SerializationManager serializationManager) {
        this.serializationManager = serializationManager;
    }

    @Autowired
    public void setOllgeiDisruptorProperties(OllgeiDisruptorProperties ollgeiDisruptorProperties) {
        this.ollgeiDisruptorProperties = ollgeiDisruptorProperties;
    }

    @Autowired
    public void setOllgeiProperties(OllgeiProperties ollgeiProperties) {
        this.ollgeiProperties = ollgeiProperties;
    }
}
