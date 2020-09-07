package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyMidstreamService<C extends RetryableContext, U extends RetryableMidstreamResponse>
        extends AbstractRetryableService<C, RetryableUpstreamResponse, U, RetryableDownstreamResponse> {

    @Override
    public RetryableUpstreamResponse upstream(C context) {
        return RetryableUpstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public U midstream(C context, RetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public RetryableDownstreamResponse downstream(C context, RetryableUpstreamResponse uResponse, U mResponse) {
        return RetryableDownstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public void writeResponse(C context, RetryableUpstreamResponse uResponse, U mResponse, RetryableDownstreamResponse dResponse, int state) {
        writeResponse(context, mResponse, state);
    }

    @Override
    public void writeUpstreamResponse(C context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeDownstreamResponse(C context, RetryableDownstreamResponse response, int state) {

    }

    @Override
    public RetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }

    /**
     * midstream.
     * @param context context
     * @return
     */
    abstract public U midstream(C context);

    /**
     * 更新upstream状态.
     * @param context object
     * @param mResponse midstream response
     * @param state state
     * @return
     */
    abstract public void writeResponse(C context, U mResponse, int state);
}
