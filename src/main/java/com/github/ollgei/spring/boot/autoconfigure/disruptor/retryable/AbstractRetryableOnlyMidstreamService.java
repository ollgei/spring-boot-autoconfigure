package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

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
        return RetryableUpstreamResponse.from(RetryableResultEnum.NOOP);
    }

    @Override
    public U midstream(RetryableContext context, RetryableUpstreamResponse uResponse) {
        return midstream(context);
    }

    @Override
    public RetryableDownstreamResponse downstream(RetryableContext context, RetryableUpstreamResponse uResponse, U mResponse) {
        return RetryableDownstreamResponse.from(RetryableResultEnum.NOOP);
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
     * midstream.
     * @param context context
     * @return
     */
    abstract public U midstream(RetryableContext context);

}
