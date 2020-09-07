package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneMidstreamService<C extends RetryableContext,T extends RetryableUpstreamResponse, S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<C, T, RetryableMidstreamResponse, S> {

    @Override
    public RetryableMidstreamResponse midstream(C context, T uResponse) {
        return RetryableMidstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public S downstream(C context, T uResponse, RetryableMidstreamResponse mResponse) {
        return downstream(context, uResponse);
    }


    @Override
    public void writeMidstreamResponse(C context, RetryableMidstreamResponse response, int state) {

    }

    @Override
    public RetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }

    /**
     * downstream.
     * @param context context
     * @param uResponse uResponse
     * @return
     */
    abstract public S downstream(C context, T uResponse);
}
