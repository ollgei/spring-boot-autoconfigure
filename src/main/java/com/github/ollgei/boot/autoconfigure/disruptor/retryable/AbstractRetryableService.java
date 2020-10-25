package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.github.ollgei.base.commonj.utils.CommonHelper;
import com.github.ollgei.boot.autoconfigure.core.OllgeiProperties;
import com.github.ollgei.boot.autoconfigure.disruptor.RetryableProperties;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
import com.github.ollgei.boot.autoconfigure.serialization.SerializationManager;
import com.github.ollgei.boot.autoconfigure.serialization.SerializationObject;
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

    private RetryableObjectRepository retryableObjectRepository;

    private RetryableBytesRepository retryableBytesRepository;

    private SerializationManager serializationManager;

    private RetryableProperties retryableProperties;

    private OllgeiProperties ollgeiProperties;

    @Override
    public void publish(RetryableContext context, CountDownLatch countDownLatch) {
        check();
        Assert.notNull(publisher, "OllgeiDisruptorPublisher not null!");
        final RetryableSubscription subscription = new RetryableSubscription();
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
            retryableObjectRepository.remove(context);
        }
    }

    private void readInternal(RetryableContext context) {
        final RetryableObjectModel model = readModel(context);
        if (model == null) {
            return;
        }
        final RetryableStateEnum rState = RetryableStateEnum.resolve(model.getState());
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
                writeFailState(context, model, state & RetryableStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = (T) model.getUpstreamResponse();
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
                writeFailState(context, model, state & RetryableStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        } else {
            mResponse = (U) model.getMidstreamResponse();
        }

        //判断第3位是0
        final S dResponse;
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            dResponse = downstream(context, uResponse, mResponse);
            if (dResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
                writeDownstreamResponse(context, dResponse, state);
                //清理数据
                cleanup(context);
                return;
            } else if (dResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else {
                writeFailState(context, model, state & RetryableStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
        //最后写入state
        if (log.isInfoEnabled()) {
            log.info("All finished!!! state:{}, starting cleanup", state);
        }
        //清理数据
        cleanup(context);
    }

    @Override
    public void write(RetryableContext context) {
        check();
        final RetryableModel model = createRetryableModel();
        if (!CommonHelper.hasText(context.getAppId())) {
            context.setAppId(ollgeiProperties.getAppId());
        }
        model.setAppId(context.getAppId());
        model.setKind(context.getKind());
        model.setTag(context.getTag());
        model.setSeqNo(context.getSeqNo().shortValue());
        model.setState(RetryableStateEnum.INIT.getCode());
        model.setRetryCount(0);
        model.setNextRetryTimestamp(0L);
        if (canBinary()) {
            ((RetryableBytesModel) model).setParams(serializationManager.serializeNativeObject(context.resolveParams()));
            retryableBytesRepository.save(context, (RetryableBytesModel) model);
        } else {
            ((RetryableObjectModel) model).setParams(context.resolveParams());
            retryableObjectRepository.save(context, (RetryableObjectModel) model);
        }
    }

    private void writeFailState(RetryableContext context, RetryableModel oriModel, int state) {
        check();
        //最后一次还是失败
        final int retryCount = oriModel.getRetryCount();
        if (retryCount > retryableProperties.getMaxAttempts()) {
            if (canBinary()) {
                retryableBytesRepository.manual(context, (RetryableBytesModel) oriModel, state);
            } else {
                retryableObjectRepository.manual(context, (RetryableObjectModel) oriModel, state);
            }
            //清理数据
            cleanup(context);
            return;
        }
        final RetryableModel model = createRetryableModel();
        model.setState(state);
        model.setNextRetryIncrTimestamp(getNextDelay());
        model.setRetryIncrCount(1);
        model.setRetryCount(retryCount + 1);
        if (canBinary()) {
            retryableBytesRepository.update(context, (RetryableBytesModel) model);
        } else {
            retryableObjectRepository.update(context, (RetryableObjectModel) model);
        }
    }

    @Override
    public void writeResponse(RetryableContext context, T uResponse, U mResponse, S dResponse, int state) {
        check();
        final RetryableModel model = createRetryableModel();
        model.setState(state);
        if (Objects.nonNull(context.resolveResponse())) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(context.resolveResponse()).build()));
            } else {
                ((RetryableObjectModel) model).setResponse(context.resolveResponse());
            }
        }
        if (Objects.nonNull(uResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setUpstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(uResponse).build()));
            } else {
                ((RetryableObjectModel) model).setUpstreamResponse(uResponse);
            }
        }
        if (Objects.nonNull(mResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setMidstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(mResponse).build()));
            } else {
                ((RetryableObjectModel) model).setMidstreamResponse(mResponse);
            }
        }
        if (Objects.nonNull(dResponse)) {
            if (canBinary()) {
                ((RetryableBytesModel) model).setDownstreamResponse(serializationManager.serializeObject(
                        SerializationObject.builder().object(dResponse).build()));
            } else {
                ((RetryableObjectModel) model).setDownstreamResponse(dResponse);
            }
        }
        if (canBinary()) {
            retryableBytesRepository.update(context, (RetryableBytesModel) model);
        } else {
            retryableObjectRepository.update(context, (RetryableObjectModel) model);
        }
    }

    private RetryableObjectModel readModel(RetryableContext context) {
        check();
        if (canBinary()) {
            RetryableObjectModel model = new RetryableObjectModel();
            RetryableModel oldModel;
            final RetryableBytesModel bytesModel = retryableBytesRepository.readModel(context);
            oldModel = bytesModel;
            if (bytesModel.getParams() != null) {
                model.setParams(
                        serializationManager.deserializeObject(bytesModel.getParams(),
                                context.resolveParamsType()));
            }
            if (bytesModel.getResponse() != null) {
                model.setResponse(
                        serializationManager.deserializeObject(bytesModel.getResponse(),
                                context.resolveResponseType()));
            }
            if (bytesModel.getMidstreamResponse() != null) {
                model.setMidstreamResponse(
                        serializationManager.deserializeObject(bytesModel.getMidstreamResponse(),
                                getMidstreamResponseClass()));
            }
            if (bytesModel.getUpstreamResponse() != null) {
                model.setUpstreamResponse(
                        serializationManager.deserializeObject(bytesModel.getUpstreamResponse(),
                                getUpstreamResponseClass()));
            }
            if (bytesModel.getDownstreamResponse() != null) {
                model.setDownstreamResponse(
                        serializationManager.deserializeObject(bytesModel.getDownstreamResponse(),
                                getDownstreamResponseClass()));
            }


            model.setKind(oldModel.getKind());
            model.setAppId(oldModel.getAppId());
            model.setTag(oldModel.getTag());
            model.setSeqNo(oldModel.getSeqNo());
            model.setState(oldModel.getState());
            model.setRetryCount(oldModel.getRetryCount());
            model.setNextRetryTimestamp(oldModel.getNextRetryTimestamp());
            model.setRetryIncrCount(oldModel.getRetryIncrCount());
            model.setNextRetryIncrTimestamp(oldModel.getNextRetryTimestamp());

            return model;
        }
        return retryableObjectRepository.readModel(context);
    }


    private long getNextDelay() {
        return new Double(retryableProperties.getDelay() * retryableProperties.getMultiplier()).longValue();
    }

    private void check() {
        if (canBinary()) {
            Objects.requireNonNull(retryableBytesRepository, "RetryableBytesRepository is not null");
        } else {
            Objects.requireNonNull(retryableObjectRepository, "RetryableObjectRepository is not null");
        }
    }

    private RetryableModel createRetryableModel() {
        if (canBinary()) {
            return new RetryableBytesModel();
        }
        return new RetryableObjectModel();
    }

    @Autowired
    public void setSerializationManager(SerializationManager serializationManager) {
        this.serializationManager = serializationManager;
    }

    @Autowired
    public void setRetryableProperties(RetryableProperties retryableProperties) {
        this.retryableProperties = retryableProperties;
    }

    @Autowired
    public void setOllgeiProperties(OllgeiProperties ollgeiProperties) {
        this.ollgeiProperties = ollgeiProperties;
    }

    @Autowired(required = false)
    public void setRetryableObjectRepository(RetryableObjectRepository retryableObjectRepository) {
        this.retryableObjectRepository = retryableObjectRepository;
    }

    @Autowired(required = false)
    public void setRetryableBytesRepository(RetryableBytesRepository retryableBytesRepository) {
        this.retryableBytesRepository = retryableBytesRepository;
    }

    @Autowired
    public void setPublisher(RetryablePublisher retryablePublisher) {
        this.publisher = retryablePublisher.getPublisher();
    }
}
