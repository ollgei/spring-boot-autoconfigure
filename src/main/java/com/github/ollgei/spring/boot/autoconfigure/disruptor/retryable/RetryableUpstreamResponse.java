package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * result.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class RetryableUpstreamResponse {
    /**result*/
    private RetryableResultEnum result;

    public static RetryableUpstreamResponse from(RetryableResultEnum resultEnum) {
        final RetryableUpstreamResponse response = new RetryableUpstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
