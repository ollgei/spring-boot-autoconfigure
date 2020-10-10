package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * single异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractSingleRetryableService<U extends RetryableMidstreamResponse>
        extends AbstractRetryableService<RetryableUpstreamResponse, U, RetryableDownstreamResponse> {

    @Override
    public RetryableUpstreamResponse upstream(RetryableContext context) {
        return RetryableResponse.noop(new RetryableUpstreamResponse());
    }

    @Override
    public U midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return handle(context);
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
    public RetryableUpstreamResponse readUpstreamResponse(RetryableContext context) {
        return null;
    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(RetryableContext context) {
        return null;
    }

    /**
     * handle.
     * @param context context
     * @return
     */
    abstract public U handle(RetryableContext context);

}
