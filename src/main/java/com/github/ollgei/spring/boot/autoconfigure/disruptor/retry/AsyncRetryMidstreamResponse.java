package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import lombok.Data;
import lombok.ToString;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@ToString
public class AsyncRetryMidstreamResponse {
    /**result*/
    private AsyncRetryResultEnum result;

    public static AsyncRetryMidstreamResponse from(AsyncRetryResultEnum resultEnum) {
        final AsyncRetryMidstreamResponse response = new AsyncRetryMidstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
