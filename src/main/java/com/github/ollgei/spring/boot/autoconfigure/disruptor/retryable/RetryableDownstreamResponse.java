package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;
import lombok.ToString;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@ToString
public class RetryableDownstreamResponse {
    /**result*/
    private RetryableResultEnum result;

    public static RetryableDownstreamResponse from(RetryableResultEnum resultEnum) {
        final RetryableDownstreamResponse response = new RetryableDownstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
