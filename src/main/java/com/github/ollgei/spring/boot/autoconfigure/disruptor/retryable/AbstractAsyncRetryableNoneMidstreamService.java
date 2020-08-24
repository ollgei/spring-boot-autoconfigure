package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableNoneMidstreamService<C extends OllgeiDisruptorContext,T extends AsyncRetryableUpstreamResponse, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, T, AsyncRetryableMidstreamResponse, S> {

    @Override
    public AsyncRetryableMidstreamResponse midstream(C context, T uResponse) {
        return AsyncRetryableMidstreamResponse.from(AsyncRetryableResultEnum.NOOP);
    }

    @Override
    public void writeMidstreamResponse(C context, AsyncRetryableMidstreamResponse response, int state) {

    }

    @Override
    public AsyncRetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }
}
