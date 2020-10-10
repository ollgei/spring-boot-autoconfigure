package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableNoneDownstreamService<T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse>
        extends AbstractRetryableService<T, U, RetryableDownstreamResponse> {

    @Override
    public RetryableDownstreamResponse downstream(RetryableContext context, T uResponse, U mResponse) {
        return RetryableResponse.noop(new RetryableDownstreamResponse());
    }

    @Override
    public void writeDownstreamResponse(RetryableContext context, RetryableDownstreamResponse response, int state) {

    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(RetryableContext context) {
        return null;
    }
}
