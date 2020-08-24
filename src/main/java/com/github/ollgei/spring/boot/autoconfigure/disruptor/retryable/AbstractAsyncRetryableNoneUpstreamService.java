package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableNoneUpstreamService<C extends OllgeiDisruptorContext, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, AsyncRetryableUpstreamResponse, U, S> {

    @Override
    public AsyncRetryableUpstreamResponse upstream(C context) {
        return AsyncRetryableUpstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeUpstreamResponse(C context, AsyncRetryableUpstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }
}
