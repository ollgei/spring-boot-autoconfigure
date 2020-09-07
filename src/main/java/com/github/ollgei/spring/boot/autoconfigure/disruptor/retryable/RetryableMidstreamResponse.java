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
public class RetryableMidstreamResponse {
    /**result*/
    private RetryableResultEnum result;

    public static RetryableMidstreamResponse from(RetryableResultEnum resultEnum) {
        final RetryableMidstreamResponse response = new RetryableMidstreamResponse();
        response.setResult(resultEnum);
        return response;
    }
}
