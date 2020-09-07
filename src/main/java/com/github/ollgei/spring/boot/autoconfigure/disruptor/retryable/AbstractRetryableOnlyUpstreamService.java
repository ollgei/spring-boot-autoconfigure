package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

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
        return RetryableMidstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public RetryableDownstreamResponse downstream(RetryableContext context, T uResponse, RetryableMidstreamResponse mResponse) {
        return RetryableDownstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public void writeMidstreamResponse(RetryableContext context, RetryableMidstreamResponse response, int state) {
    }

    @Override
    public void writeDownstreamResponse(RetryableContext context, RetryableDownstreamResponse response, int state) {
    }

    @Override
    public RetryableMidstreamResponse readMidstreamResponse(RetryableContext context) {
        return null;
    }

    @Override
    public RetryableDownstreamResponse readDownstreamResponse(RetryableContext context) {
        return null;
    }
}
