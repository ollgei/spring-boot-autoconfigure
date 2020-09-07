package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneUpstreamService<C extends RetryableContext, U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<C, RetryableUpstreamResponse, U, S> {

    @Override
    public RetryableUpstreamResponse upstream(C context) {
        return RetryableUpstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public U midstream(C context, RetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public S downstream(C context, RetryableUpstreamResponse uResponse, U mResponse) {
        return downstream(context, mResponse);
    }

    @Override
    public void writeResponse(C context, RetryableUpstreamResponse uResponse, U mResponse, S dResponse, int state) {
        writeResponse(context, mResponse, dResponse, state);
    }

    @Override
    public void writeUpstreamResponse(C context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public RetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }

    /**
     * midstream.
     * @param context context
     * @return
     */
    abstract public U midstream(C context);
    /**
     * downstream.
     * @param context context
     * @param mResponse mResponse
     * @return
     */
    abstract public S downstream(C context, U mResponse);
    /**
     * 更新upstream状态.
     * @param context object
     * @param mResponse midstream response
     * @param dResponse downstream response
     * @param state state
     * @return
     */
    abstract public void writeResponse(C context, U mResponse, S dResponse, int state);
}
