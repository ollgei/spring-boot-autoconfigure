package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * result.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryUpstreamResponse {
    /**result*/
    private AsyncRetryResultEnum result;

    public static AsyncRetryUpstreamResponse from(AsyncRetryResultEnum resultEnum) {
        final AsyncRetryUpstreamResponse response = new AsyncRetryUpstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
