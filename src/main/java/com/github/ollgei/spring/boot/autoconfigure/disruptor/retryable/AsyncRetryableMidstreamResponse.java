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
public class AsyncRetryableMidstreamResponse {
    /**result*/
    private AsyncRetryableResultEnum result;

    public static AsyncRetryableMidstreamResponse from(AsyncRetryableResultEnum resultEnum) {
        final AsyncRetryableMidstreamResponse response = new AsyncRetryableMidstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
