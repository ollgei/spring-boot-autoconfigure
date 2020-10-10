package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
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

    private RetryableStringRepository retryableStringRepository;

    private RetryableBytesRepository retryableBytesRepository;

    private SerializationManager serializationManager;

    private OllgeiDisruptorProperties ollgeiDisruptorProperties;

    private OllgeiProperties ollgeiProperties;

    private final Gson gson;

    public AbstractRetryableService() {
        this(new GsonBuilder().create());
    }

    public AbstractRetryableService(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void publish(RetryableContext context, CountDownLatch countDownLatch) {
        check();
        Assert.notNull(publisher, "OllgeiDisruptorPublisher not null!");
        final SpringOllgeiDisruptorSubscription subscription = new SpringOllgeiDisruptorSubscription();
        subscription.setContext(context);
        Optional.ofNullable(countDownLatch).ifPresent(l -> subscription.setCountDownLatch(l));
        subscription.setClazz(this.getClass());
        publisher.write(subscription);
    }

    @Override
    public void read(RetryableContext context, CountDownLatch countDownLatch) {
        try {
            check();
            readInternal(context);
        } finally {
            Optional.ofNullable(countDownLatch).
                    filter(l -> l.getCount() > 0).ifPresent(l -> {
                        l.countDown();
                    });
        }
    }

    @Override
    public void cleanup(RetryableContext context) {
        check();
        if (canBinary()) {
            retryableBytesRepository.remove(context);
        } else {
            retryableStringRepository.remove(context);
        }
    }

    private void readInternal(RetryableContext context) {
        final RetryableStateEnum rState = readState(context);
        if (rState == RetryableStateEnum.SUCCESS) {
            if (log.isInfoEnabled()) {
                log.info("【异步重试】已经全部执行完成");
            }
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
        //清理数据
        cleanup(context);
    }

    @Override
    public void write(RetryableContext context) {
        check();
        final RetryableModel model = createRetryableModel(context);
        if (!CommonHelper.hasText(context.getAppId())) {
            context.setAppId(ollgeiProperties.getAppId());
        }
        model.setAppId(context.getAppId());
        model.setBizKind(context.getBizKind());
        model.setBizId(context.getBizId());
        model.setBizSeqNo(context.getBizSeqNo().shortValue());
        model.setState(RetryableStateEnum.INIT.getCode());
        model.setRetryCount(0);
        model.setNextRetryTimestamp(0L);
        if (canBinary()) {
            ((RetryableBytesModel) model).setParams(serializationManager.serializeNativeObject(context.getData()));
            retryableBytesRepository.save(context, (RetryableBytesModel) model);
        } else {
            ((RetryableStringModel) model).setParams(gson.toJson(context.getData()));
            retryableStringRepository.save(context, (RetryableStringModel) model);
        }
    }

    @Override
    public RetryableStateEnum readState(RetryableContext context) {
        check();
        return RetryableStateEnum.resolve(canBinary() ?
                retryableBytesRepository.readState(context) :
                retryableStringRepository.readState(context));
    }

    @Override
    public void writeState(RetryableContext context, int state, boolean success) {
        check();
        final RetryableModel model = createRetryableModel(context);
        model.setState(state);
        if (!success) {
            model.setNextRetryIncrTimestamp(getNextDelay());
            model.setRetryIncrCount(1);
        }
        if (canBinary()) {
            retryableBytesRepository.update(context, (RetryableBytesModel) model);
        } else {
            retryableStringRepository.update(context, (RetryableStringModel) model);
        }
    }

    @Override
    public void writeResponse(RetryableContext context, T uResponse, U mResponse, S dResponse, int state) {
        check();
        final RetryableModel model = createRetryableModel(context);
        model.setState(state);
        if (Objects.nonNull(context.getResponseData())) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(context.getResponseData()).build()));
            } else {
                ((RetryableStringModel) model).setResponse(gson.toJson(context.getResponseData()));
            }
        }
        if (Objects.nonNull(uResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setUpstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(uResponse).build()));
            } else {
                ((RetryableStringModel) model).setUpstreamResponse(gson.toJson(uResponse));
            }
        }
        if (Objects.nonNull(mResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setMidstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(mResponse).build()));
            } else {
                ((RetryableStringModel) model).setMidstreamResponse(gson.toJson(mResponse));
            }
        }
        if (Objects.nonNull(dResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setDownstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(dResponse).build()));
            } else {
                ((RetryableStringModel) model).setDownstreamResponse(gson.toJson(dResponse));
            }
        }
        if (canBinary()) {
            retryableBytesRepository.update(context, (RetryableBytesModel) model);
        } else {
            retryableStringRepository.update(context, (RetryableStringModel) model);
        }
    }

    private long getNextDelay() {
        final OllgeiDisruptorProperties.Retryable retryableProps = ollgeiDisruptorProperties.getRetryable();
        return new Double(retryableProps.getDelay() * retryableProps.getMultiplier()).longValue();
    }

    private void check() {
        if (canBinary()) {
            Objects.requireNonNull(retryableBytesRepository, "RetryableBytesRepository is not null");
        } else {
            Objects.requireNonNull(retryableStringRepository, "RetryableStringRepository is not null");
        }
    }

    @Override
    public T readUpstreamResponse(RetryableContext context, Class<T> cls) {
        check();
        if (canBinary()) {
            final byte[] response = retryableBytesRepository.readUpstreamResponse(context);
            if (Objects.isNull(response) || response.length == 0) {
                return null;
            }
            return serializationManager.deserializeObject(response, cls);
        }
        return gson.fromJson(retryableStringRepository.readUpstreamResponse(context), cls);
    }

    @Override
    public U readMidstreamResponse(RetryableContext context, Class<U> cls) {
        check();
        if (canBinary()) {
            final byte[] response = retryableBytesRepository.readMidstreamResponse(context);
            if (Objects.isNull(response) || response.length == 0) {
                return null;
            }
            return serializationManager.deserializeObject(response, cls);
        }
        return gson.fromJson(retryableStringRepository.readMidstreamResponse(context), cls);
    }

    @Override
    public S readDownstreamResponse(RetryableContext context, Class<S> cls) {
        check();
        if (canBinary()) {
            final byte[] response = retryableBytesRepository.readDownstreamResponse(context);
            if (Objects.isNull(response) || response.length == 0) {
                return null;
            }
            return serializationManager.deserializeObject(response, cls);
        }
        return gson.fromJson(retryableStringRepository.readDownstreamResponse(context), cls);
    }

    private RetryableModel createRetryableModel(RetryableContext context) {
        return canBinary() ? new RetryableBytesModel() : new RetryableStringModel();
    }

    public OllgeiDisruptorPublisher getPublisher() {
        return publisher;
    }

    @Autowired
    public void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
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

    @Autowired(required = false)
    public void setRetryableStringRepository(RetryableStringRepository retryableStringRepository) {
        this.retryableStringRepository = retryableStringRepository;
    }

    @Autowired(required = false)
    public void setRetryableBytesRepository(RetryableBytesRepository retryableBytesRepository) {
        this.retryableBytesRepository = retryableBytesRepository;
    }
}
