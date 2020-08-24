package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyUpstreamService<C extends OllgeiDisruptorContext,T extends AsyncRetryableUpstreamResponse>
        extends AbstractAsyncRetryableService<C, T, AsyncRetryableMidstreamResponse, AsyncRetryableDownstreamResponse> {

    @Override
    public AsyncRetryableMidstreamResponse midstream(C context, T uResponse) {
        return AsyncRetryableMidstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public AsyncRetryableDownstreamResponse downstream(C context, T uResponse, AsyncRetryableMidstreamResponse mResponse) {
        return AsyncRetryableDownstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeMidstreamResponse(C context, AsyncRetryableMidstreamResponse response, int state) {
    }

    @Override
    public void writeDownstreamResponse(C context, AsyncRetryableDownstreamResponse response, int state) {
    }

    @Override
    public AsyncRetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }

    @Override
    public AsyncRetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }
}
