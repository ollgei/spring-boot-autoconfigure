package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableNoneDownstreamService<C extends AsyncRetryableContext,T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse>
        extends AbstractAsyncRetryableService<C, T, U, AsyncRetryableDownstreamResponse> {

    @Override
    public AsyncRetryableDownstreamResponse downstream(C context, T uResponse, U mResponse) {
        return AsyncRetryableDownstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeResponse(C context, T uResponse, U mResponse, AsyncRetryableDownstreamResponse dResponse, int state) {
        writeResponse(context, uResponse, mResponse, state);
    }

    @Override
    public void writeDownstreamResponse(C context, AsyncRetryableDownstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableDownstreamResponse readDownstreamResponse(C context) {
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
