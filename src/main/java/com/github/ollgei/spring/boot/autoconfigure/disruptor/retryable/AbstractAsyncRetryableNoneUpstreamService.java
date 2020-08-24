package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableNoneUpstreamService<C extends AsyncRetryableContext, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, AsyncRetryableUpstreamResponse, U, S> {

    @Override
    public AsyncRetryableUpstreamResponse upstream(C context) {
        return AsyncRetryableUpstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public U midstream(C context, AsyncRetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public S downstream(C context, AsyncRetryableUpstreamResponse uResponse, U mResponse) {
        return downstream(context, mResponse);
    }

    @Override
    public void writeUpstreamResponse(C context, AsyncRetryableUpstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableUpstreamResponse readUpstreamResponse(C context) {
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
}
