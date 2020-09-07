package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyDownstreamService<S extends RetryableDownstreamResponse>
        extends AbstractRetryableService<RetryableUpstreamResponse, RetryableMidstreamResponse, S> {

    @Override
    public RetryableUpstreamResponse upstream(RetryableContext context) {
        return RetryableUpstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public RetryableMidstreamResponse midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return RetryableMidstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public S downstream(RetryableContext context, RetryableUpstreamResponse uResponse, RetryableMidstreamResponse mResponse) {
        return downstream(context);
    }

    @Override
    public void writeUpstreamResponse(RetryableContext context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeMidstreamResponse(RetryableContext context, RetryableMidstreamResponse response, int state) {

    }

    @Override
    public RetryableUpstreamResponse readUpstreamResponse(RetryableContext context) {
        return null;
    }

    @Override
    public RetryableMidstreamResponse readMidstreamResponse(RetryableContext context) {
        return null;
    }

    /**
     * downstream.
     * @param context context
     * @return
     */
    abstract public S downstream(RetryableContext context);

}
