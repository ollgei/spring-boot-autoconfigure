package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyDownstreamService<C extends AsyncRetryableContext, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, AsyncRetryableUpstreamResponse, AsyncRetryableMidstreamResponse, S> {

    @Override
    public AsyncRetryableUpstreamResponse upstream(C context) {
        return AsyncRetryableUpstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public AsyncRetryableMidstreamResponse midstream(C context, AsyncRetryableUpstreamResponse uResponse) {
        return AsyncRetryableMidstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public S downstream(C context, AsyncRetryableUpstreamResponse uResponse, AsyncRetryableMidstreamResponse mResponse) {
        return downstream(context);
    }

    @Override
    public void writeResponse(C context, AsyncRetryableUpstreamResponse uResponse, AsyncRetryableMidstreamResponse mResponse, S dResponse, int state) {
        writeResponse(context, dResponse, state);
    }

    @Override
    public void writeUpstreamResponse(C context, AsyncRetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeMidstreamResponse(C context, AsyncRetryableMidstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public AsyncRetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }

    /**
     * downstream.
     * @param context context
     * @return
     */
    abstract public S downstream(C context);

    /**
     * 更新upstream状态.
     * @param context object
     * @param dResponse downstream response
     * @param state state
     * @return
     */
    abstract void writeResponse(C context, S dResponse, int state);
}
