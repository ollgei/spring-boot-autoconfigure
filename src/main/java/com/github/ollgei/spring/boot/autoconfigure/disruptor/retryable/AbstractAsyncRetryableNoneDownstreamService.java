package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableNoneDownstreamService<C extends OllgeiDisruptorContext,T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse>
        extends AbstractAsyncRetryableService<C, T, U, AsyncRetryableDownstreamResponse> {

    @Override
    public AsyncRetryableDownstreamResponse downstream(C context, T uResponse, U mResponse) {
        return AsyncRetryableDownstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeDownstreamResponse(C context, AsyncRetryableDownstreamResponse response) {

    }

    @Override
    public AsyncRetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }
}
