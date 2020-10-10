package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneUpstreamService<U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<RetryableUpstreamResponse, U, S> {

    @Override
    public RetryableUpstreamResponse upstream(RetryableContext context) {
        return RetryableResponse.noop(new RetryableUpstreamResponse());
    }

    @Override
    public U midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public S downstream(RetryableContext context, RetryableUpstreamResponse uResponse, U mResponse) {
        return downstream(context, mResponse);
    }

    @Override
    public void writeUpstreamResponse(RetryableContext context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public RetryableUpstreamResponse readUpstreamResponse(RetryableContext context) {
        return null;
    }

    /**
     * midstream.
     * @param context context
     * @return
     */
    abstract public U midstream(RetryableContext context);
    /**
     * downstream.
     * @param context context
     * @param mResponse mResponse
     * @return
     */
    abstract public S downstream(RetryableContext context, U mResponse);
}
