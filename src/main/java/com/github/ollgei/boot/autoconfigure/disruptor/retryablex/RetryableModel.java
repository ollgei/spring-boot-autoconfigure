package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
public class RetryableModel<T> {
    private boolean sync;
    private int state;
    private String key;
    private int retryCount;
    private long nextRetryMills;
    private T request;
    private T response;
    private T upstreamResponse;
    private T midstreamResponse;
    private T downstreamResponse;

    public RetryableModel<T> deepCopy() {
        final RetryableModel<T> copy = new RetryableModel<T>();
        copy.setSync(isSync());
        copy.setState(getState());
        copy.setKey(getKey());
        copy.setRetryCount(getRetryCount());
        copy.setNextRetryMills(getNextRetryMills());
        copy.setRequest(getRequest());
        copy.setResponse(getResponse());
        copy.setUpstreamResponse(getUpstreamResponse());
        copy.setMidstreamResponse(getMidstreamResponse());
        copy.setDownstreamResponse(getDownstreamResponse());
        return copy;
    }
}