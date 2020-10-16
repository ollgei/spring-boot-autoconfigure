package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyMidstreamService<U extends RetryableMidstreamResponse>
        extends AbstractRetryableService<RetryableUpstreamResponse, U, RetryableDownstreamResponse> {

    @Override
    public RetryableUpstreamResponse upstream(RetryableContext context) {
        return RetryableResponse.noop(new RetryableUpstreamResponse());
    }

    @Override
    public U midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public RetryableDownstreamResponse downstream(RetryableContext context, RetryableUpstreamResponse uResponse, U mResponse) {
        return RetryableResponse.noop(new RetryableDownstreamResponse());
    }

    @Override
    public void writeUpstreamResponse(RetryableContext context, RetryableUpstreamResponse response, int state) {

    }

    @Override
    public void writeDownstreamResponse(RetryableContext context, RetryableDownstreamResponse response, int state) {

    }

    @Override
    public Class<?> getUpstreamResponseClass() {
        return null;
    }

    @Override
    public Class<?> getDownstreamResponseClass() {
        return null;
    }

    /**
     * midstream.
     * @param context context
     * @return
     */
    abstract public U midstream(RetryableContext context);

}
