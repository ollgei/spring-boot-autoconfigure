package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractRetryableOnlyUpstreamService<T extends RetryableUpstreamResponse>
        extends AbstractRetryableService<T, RetryableMidstreamResponse, RetryableDownstreamResponse> {

    @Override
    public RetryableMidstreamResponse midstream(RetryableContext context, T uResponse) {
        return RetryableResponse.noop(new RetryableMidstreamResponse());
    }

    @Override
    public RetryableDownstreamResponse downstream(RetryableContext context, T uResponse, RetryableMidstreamResponse mResponse) {
        return RetryableResponse.noop(new RetryableDownstreamResponse());
    }

    @Override
    public void writeMidstreamResponse(RetryableContext context, RetryableMidstreamResponse response, int state) {
    }

    @Override
    public void writeDownstreamResponse(RetryableContext context, RetryableDownstreamResponse response, int state) {
    }

    @Override
    public Class<?> getMidstreamResponseClass() {
        return null;
    }

    @Override
    public Class<?> getDownstreamResponseClass() {
        return null;
    }
}
