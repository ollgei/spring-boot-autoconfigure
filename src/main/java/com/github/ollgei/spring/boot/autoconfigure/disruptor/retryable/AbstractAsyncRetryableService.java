package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import org.springframework.util.Assert;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSimpleSubscription;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractAsyncRetryableService<C extends AsyncRetryableContext, T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse>
        implements AsyncRetryableService<C, T, U, S> {

    private OllgeiDisruptorPublisher publisher;

    @Override
    public void publish(C context) {
        Assert.notNull(publisher, "OllgeiDisruptorPublisher not null!");
        final OllgeiDisruptorSimpleSubscription subscription = new OllgeiDisruptorSimpleSubscription();
        subscription.setContext(context);
        subscription.setKind(kind());
        publisher.write(subscription);
    }

    @Override
    public void read(C context) {
        final AsyncRetryableStateEnum rState = readState(context);
        int state = (rState == AsyncRetryableStateEnum.INIT) ? AsyncRetryableStateEnum.UPSTREAM_FAIL.getCode() : rState.getCode();
        final T uResponse;

        if (AsyncRetryableStateEnum.hasFail(state, AsyncRetryableStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            uResponse = upstream(context);
            if (uResponse.getResult() == AsyncRetryableResultEnum.SUCCESS) {
                writeUpstreamResponse(context, uResponse);
                state = state | AsyncRetryableStateEnum.UPSTREAM_SUCCESS.getCode();
            } else if (uResponse.getResult() == AsyncRetryableResultEnum.NOOP) {
                state = state | AsyncRetryableStateEnum.UPSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state | AsyncRetryableStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = readUpstreamResponse(context);
        }

        //2 执行中游业务处理 保持幂等性(本地处理)
        final U mResponse;

        if (AsyncRetryableStateEnum.hasFail(state, AsyncRetryableStateEnum.MIDSTREAM_FAIL)) {
            mResponse = midstream(context, uResponse);
            if (mResponse.getResult() == AsyncRetryableResultEnum.SUCCESS) {
                writeMidstreamResponse(context, mResponse);
                state = state | AsyncRetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else if (mResponse.getResult() == AsyncRetryableResultEnum.NOOP) {
                state = state | AsyncRetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state | AsyncRetryableStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        } else {
            mResponse = readMidstreamResponse(context);
        }

        //判断第3位是0
        final S dResponse;
        if (AsyncRetryableStateEnum.hasFail(state, AsyncRetryableStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            dResponse = downstream(context, uResponse, mResponse);
            if (dResponse.getResult() == AsyncRetryableResultEnum.SUCCESS) {
                writeDownstreamResponse(context, dResponse);
                state = state | AsyncRetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else if (dResponse.getResult() == AsyncRetryableResultEnum.NOOP) {
                state = state | AsyncRetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else {
                state = state | AsyncRetryableStateEnum.DOWNSTREAM_FAIL.getCode();
            }
        }
        //最后写入state
        writeState(context, state);
    }
}
