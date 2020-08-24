package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyUpstreamService<C extends OllgeiDisruptorContext,T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, T, U, S> {

    @Override
    public U midstream(C context, T uResponse) {
        return null;
    }

    @Override
    public S downstream(C context, T uResponse, U mResponse) {
        return null;
    }

    @Override
    public void writeMidstreamResponse(C context, U response, int state) {
    }

    @Override
    public void writeDownstreamResponse(C context, S response, int state) {
    }

    @Override
    public U readMidstreamResponse(C context) {
        return null;
    }

    @Override
    public S readDownstreamResponse(C context) {
        return null;
    }
}
