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
public class AsyncRetryDownstreamResponse {
    /**result*/
    private AsyncRetryResultEnum result;

    public static AsyncRetryDownstreamResponse from(AsyncRetryResultEnum resultEnum) {
        final AsyncRetryDownstreamResponse response = new AsyncRetryDownstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
