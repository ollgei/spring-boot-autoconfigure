package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * result.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class RetryableUpstreamResponse extends RetryableResponse {

    public static RetryableUpstreamResponse from(RetryableResultEnum resultEnum) {
        return RetryableResponse.from(new RetryableUpstreamResponse(), resultEnum);
    }

}
