package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractAsyncRetryableOnlyMidstreamService<C extends OllgeiDisruptorContext,T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse>
        extends AbstractAsyncRetryableService<C, T, U, S> {

    @Override
    public boolean skipUpstream() {
        return true;
    }

    @Override
    public boolean skipDownstream() {
        return true;
    }

    @Override
    public T upstream(C context) {
        return null;
    }

    @Override
    public S downstream(C context, T uResponse, U mResponse) {
        return null;
    }

    @Override
    public void writeUpstreamResponse(C context, T response, int state) {

    }

    @Override
    public void writeDownstreamResponse(C context, S response, int state) {

    }

    @Override
    public T readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public S readDownstreamResponse(C context) {
        return null;
    }
}
