package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyUpstreamService<C extends RetryableContext,T extends RetryableUpstreamResponse>
        extends AbstractRetryableService<C, T, RetryableMidstreamResponse, RetryableDownstreamResponse> {

    @Override
    public RetryableMidstreamResponse midstream(C context, T uResponse) {
        return RetryableMidstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public RetryableDownstreamResponse downstream(C context, T uResponse, RetryableMidstreamResponse mResponse) {
        return RetryableDownstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public void writeResponse(C context, T uResponse, RetryableMidstreamResponse mResponse, RetryableDownstreamResponse dResponse, int state) {
        writeResponse(context, uResponse, state);
    }

    @Override
    public void writeMidstreamResponse(C context, RetryableMidstreamResponse response, int state) {
    }

    @Override
    public void writeDownstreamResponse(C context, RetryableDownstreamResponse response, int state) {
    }

    @Override
    public RetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }

    /**
     * 更新upstream状态.
     * @param context object
     * @param uResponse upstream response
     * @param state state
     * @return
     */
    abstract public void writeResponse(C context, T uResponse, int state);
}
