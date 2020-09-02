package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyMidstreamService<C extends AsyncRetryableContext, U extends AsyncRetryableMidstreamResponse>
        extends AbstractAsyncRetryableService<C, AsyncRetryableUpstreamResponse, U, AsyncRetryableDownstreamResponse> {

    @Override
    public AsyncRetryableUpstreamResponse upstream(C context) {
        return AsyncRetryableUpstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public U midstream(C context, AsyncRetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public AsyncRetryableDownstreamResponse downstream(C context, AsyncRetryableUpstreamResponse uResponse, U mResponse) {
        return AsyncRetryableDownstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeResponse(C context, AsyncRetryableUpstreamResponse uResponse, U mResponse, AsyncRetryableDownstreamResponse dResponse, int state) {
        writeResponse(context, mResponse, state);
    }

    @Override
    public void writeUpstreamResponse(C context, AsyncRetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeDownstreamResponse(C context, AsyncRetryableDownstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public AsyncRetryableDownstreamResponse readDownstreamResponse(C context) {
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
