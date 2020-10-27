package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Getter
@ToString
@Builder
public class RetryableConfiguration {

    /**
     * delay(seconds).
     */
    @Builder.Default
    private Integer delay = 10;

    /**
     * multiplier.
     */
    @Builder.Default
    private Double multiplier = 1.0d;

    /**
     * maxAttempts.
     */
    @Builder.Default
    private Integer maxAttempts = 5;

    /**
     * batchSize.
     */
    @Builder.Default
    private Integer batchSize = 80;

}
