package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyMidstreamService<C extends OllgeiDisruptorContext, U extends AsyncRetryableMidstreamResponse>
        extends AbstractAsyncRetryableService<C, AsyncRetryableUpstreamResponse, U, AsyncRetryableDownstreamResponse> {

    @Override
    public AsyncRetryableUpstreamResponse upstream(C context) {
        return AsyncRetryableUpstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public AsyncRetryableDownstreamResponse downstream(C context, AsyncRetryableUpstreamResponse uResponse, U mResponse) {
        return AsyncRetryableDownstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeUpstreamResponse(C context, AsyncRetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeDownstreamResponse(C context, AsyncRetryableDownstreamResponse response, int state) {

    }

    @Override
    public T readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public AsyncRetryableDownstreamResponse readDownstreamResponse(C context) {
        return null;
    }
}
