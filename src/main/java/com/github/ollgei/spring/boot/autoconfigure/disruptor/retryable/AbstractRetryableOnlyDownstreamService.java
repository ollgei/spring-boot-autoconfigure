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
        return RetryableResponse.noop(new RetryableUpstreamResponse());
    }

    @Override
    public RetryableMidstreamResponse midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return RetryableResponse.noop(new RetryableMidstreamResponse());
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
    public Class<?> getUpstreamResponseClass() {
        return null;
    }

    @Override
    public Class<?> getMidstreamResponseClass() {
        return null;
    }
    /**
     * downstream.
     * @param context context
     * @return
     */
    abstract public S downstream(RetryableContext context);

}
