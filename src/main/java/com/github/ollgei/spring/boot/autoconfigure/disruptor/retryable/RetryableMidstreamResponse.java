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
public class RetryableMidstreamResponse extends RetryableResponse {

    public static RetryableMidstreamResponse from(RetryableResultEnum resultEnum) {
        return RetryableResponse.from(new RetryableMidstreamResponse(), resultEnum);
    }

}
