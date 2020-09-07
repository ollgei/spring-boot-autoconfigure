package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneDownstreamService<C extends RetryableContext,T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse>
        extends AbstractRetryableService<C, T, U, RetryableDownstreamResponse> {

    @Override
    public RetryableDownstreamResponse downstream(C context, T uResponse, U mResponse) {
        return RetryableDownstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public void writeResponse(C context, T uResponse, U mResponse, RetryableDownstreamResponse dResponse, int state) {
        writeResponse(context, uResponse, mResponse, state);
    }

    @Override
    public void writeDownstreamResponse(C context, RetryableDownstreamResponse response, int state) {

    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }

    /**
     * 更新upstream状态.
     * @param context object
     * @param uResponse upstream response
     * @param mResponse midstream response
     * @param state state
     * @return
     */
    abstract public void writeResponse(C context, T uResponse, U mResponse, int state);
}
