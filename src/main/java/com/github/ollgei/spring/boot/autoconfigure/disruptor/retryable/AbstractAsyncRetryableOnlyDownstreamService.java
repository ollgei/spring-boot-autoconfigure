package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyDownstreamService<C extends OllgeiDisruptorContext, S extends AsyncRetryableDownstreamResponse>
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
}
