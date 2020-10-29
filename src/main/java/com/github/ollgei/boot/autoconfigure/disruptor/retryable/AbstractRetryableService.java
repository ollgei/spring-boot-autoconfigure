package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Slf4j
public abstract class AbstractRetryableService<T> implements RetryableService<T> {

    private RetryableRepository<T> retryableRepository;

    private RetryableConfiguration retryableConfiguration;

    public AbstractRetryableService(RetryableRepository<T> retryableRepository, RetryableConfiguration retryableConfiguration) {
        this.retryableRepository = retryableRepository;
        this.retryableConfiguration = retryableConfiguration;
    }

    @Override
    public RetryableModel<T> query(RetryableKey key) {
        return retryableRepository.query(key);
    }

    @Override
    public void insert(RetryableModel<T> model) {
        retryableRepository.insert(model);
    }

    @Override
    public void handle(RetryableModel<T> modelRequest) {
        final RetryableModel<T> model = modelRequest.deepCopy();
        final RetryableStateEnum rState = RetryableStateEnum.resolve(model.getState());
        if (rState == RetryableStateEnum.SUCCESS) {
            if (log.isInfoEnabled()) {
                log.info("【异步重试】已经全部执行完成");
            }
            return;
        }

        int state = rState.getCode();
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            final RetryableResponseModel<T> uResponse = upstream(model);
            if (uResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
                model.setUpstreamResponse(uResponse.getResponse());
                model.setState(state);
                retryableRepository.update(model);
            } else if (uResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
            } else {
                fail(model, state & RetryableStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        }

        //2 执行中游业务处理 保持幂等性(本地处理)
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.MIDSTREAM_FAIL)) {
            final RetryableResponseModel<T> mResponse = midstream(model);
            if (mResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
                model.setMidstreamResponse(mResponse.getResponse());
                model.setState(state);
                retryableRepository.update(model);
            } else if (mResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else {
                fail(model, state & RetryableStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        }

        //判断第3位是0
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final RetryableResponseModel<T> dResponse = downstream(model);
            if (dResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
                model.setDownstreamResponse(dResponse.getResponse());
                model.setState(state);
                //清理数据
                retryableRepository.remove(model);
                return;
            } else if (dResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else {
                fail(model, state & RetryableStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
        //最后写入state
        if (log.isInfoEnabled()) {
            log.info("All finished!!! state:{}, starting cleanup", state);
        }
        //清理数据
        retryableRepository.remove(model);
    }

    private void fail(RetryableModel<T> model, int state) {
        //最后一次还是失败
        model.setState(state);
        final int retryCount = model.getRetryCount();
        if (retryCount > retryableConfiguration.getMaxAttempts()) {
            //人工处理
            retryableRepository.manual(model);
            //清理数据
            retryableRepository.remove(model);
            return;
        }
        model.setNextRetryMills(getNextDelay());
        model.setRetryCount(retryCount + 1);
        retryableRepository.update(model);
    }

    private long getNextDelay() {
        return new Double(retryableConfiguration.getDelay() * retryableConfiguration.getMultiplier()).longValue();
    }
}
