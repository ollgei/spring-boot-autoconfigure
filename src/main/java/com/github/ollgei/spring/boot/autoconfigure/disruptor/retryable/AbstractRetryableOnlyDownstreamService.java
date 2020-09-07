package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyDownstreamService<C extends RetryableContext, S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<C, RetryableUpstreamResponse, RetryableMidstreamResponse, S> {

    @Override
    public RetryableUpstreamResponse upstream(C context) {
        return RetryableUpstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public RetryableMidstreamResponse midstream(C context, RetryableUpstreamResponse uResponse) {
        return RetryableMidstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public S downstream(C context, RetryableUpstreamResponse uResponse, RetryableMidstreamResponse mResponse) {
        return downstream(context);
    }

    @Override
    public void writeUpstreamResponse(C context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeMidstreamResponse(C context, RetryableMidstreamResponse response, int state) {

    }

    @Override
    public RetryableUpstreamResponse readUpstreamResponse(C context) {
        return null;
    }

    @Override
    public RetryableMidstreamResponse readMidstreamResponse(C context) {
        return null;
    }

    /**
     * downstream.
     * @param context context
     * @return
     */
    abstract public S downstream(C context);

}
