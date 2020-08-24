package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * 重试结果.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryableResult<T> {
    /**执行结果*/
    private AsyncRetryableResultEnum value;
    /**结果返回*/
    private T response;

}
