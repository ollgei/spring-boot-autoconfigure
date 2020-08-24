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
public class AsyncRetryableDownstreamResponse {
    /**result*/
    private AsyncRetryableResultEnum result;

    public static AsyncRetryableDownstreamResponse from(AsyncRetryableResultEnum resultEnum) {
        final AsyncRetryableDownstreamResponse response = new AsyncRetryableDownstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
