package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * result.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryableUpstreamResponse {
    /**result*/
    private AsyncRetryableResultEnum result;

    public static AsyncRetryableUpstreamResponse from(AsyncRetryableResultEnum resultEnum) {
        final AsyncRetryableUpstreamResponse response = new AsyncRetryableUpstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
