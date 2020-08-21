package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSimpleSubscription;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractAsyncRetryService<C extends OllgeiDisruptorContext,T extends AsyncRetryUpstreamResponse, U extends AsyncRetryMidstreamResponse>
        implements AsyncRetryService<C, T, U> {

    private OllgeiDisruptorPublisher publisher;

    @Override
    public void initAndPublish(C context) {
        //1 序列化到数据库中，方便重试使用
        init(context);
        //2 异步发布消息
        final OllgeiDisruptorSimpleSubscription subscription = new OllgeiDisruptorSimpleSubscription();
        subscription.setContext(context);
        subscription.setKind(kind());
        publisher.write(subscription);
    }

    @Override
    public void run(C context) {
        final AsyncRetryStateEnum rState = readState(context);
        int state = (rState == AsyncRetryStateEnum.INIT) ? AsyncRetryStateEnum.UPSTREAM_FAIL.getCode() : rState.getCode();
        final T uResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            uResponse = upstream(context);
            if (uResponse.getResult() == AsyncRetryResultEnum.SUCCESS) {
                writeUpstreamResponse(context, uResponse);
                state = state | AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode();
            } else if (uResponse.getResult() == AsyncRetryResultEnum.NOOP) {
                state = state | AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state | AsyncRetryStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = readUpstreamResponse(context);
        }

        //2 执行中游业务处理 保持幂等性(本地处理)
        final U mResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.MIDSTREAM_FAIL)) {
            mResponse = midstream(context, uResponse);
            if (mResponse.getResult() == AsyncRetryResultEnum.SUCCESS) {
                writeMidstreamResponse(context, mResponse);
                state = state | AsyncRetryStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else if (mResponse.getResult() == AsyncRetryResultEnum.NOOP) {
                state = state | AsyncRetryStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state | AsyncRetryStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        } else {
            mResponse = readMidstreamResponse(context);
        }

        //判断第3位是0
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final AsyncRetryResultEnum result = downstream(context, uResponse, mResponse);
            if (result == AsyncRetryResultEnum.SUCCESS || result == AsyncRetryResultEnum.NOOP) {
                state = state | AsyncRetryStateEnum.DOWNSTREAM_SUCCESS.getCode();
            }  else {
                state = state | AsyncRetryStateEnum.DOWNSTREAM_FAIL.getCode();
            }
        }
        writeState(context, state);
    }

    protected OllgeiDisruptorPublisher getPublisher() {
        return publisher;
    }

    protected void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
    }
}
