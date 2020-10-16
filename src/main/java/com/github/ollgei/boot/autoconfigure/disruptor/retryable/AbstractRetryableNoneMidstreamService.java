package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneMidstreamService<T extends RetryableUpstreamResponse, S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<T, RetryableMidstreamResponse, S> {

    @Override
    public RetryableMidstreamResponse midstream(RetryableContext context, T uResponse) {
        return RetryableResponse.noop(new RetryableMidstreamResponse());
    }

    @Override
    public S downstream(RetryableContext context, T uResponse, RetryableMidstreamResponse mResponse) {
        return downstream(context, uResponse);
    }


    @Override
    public void writeMidstreamResponse(RetryableContext context, RetryableMidstreamResponse response, int state) {

    }

    @Override
    public Class<?> getMidstreamResponseClass() {
        return null;
    }

    /**
     * downstream.
     * @param context context
     * @param uResponse uResponse
     * @return
     */
    abstract public S downstream(RetryableContext context, T uResponse);
}
