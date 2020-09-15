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
public class RetryableDownstreamResponse extends RetryableResponse {

    public static RetryableDownstreamResponse from(RetryableResultEnum resultEnum) {
        return RetryableResponse.from(new RetryableDownstreamResponse(), resultEnum);
    }
}
