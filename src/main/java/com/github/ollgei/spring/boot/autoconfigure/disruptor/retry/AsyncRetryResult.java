package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import lombok.Data;

/**
 * 重试结果.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryResult {

    private AsyncRetryResultEnum value;

}
