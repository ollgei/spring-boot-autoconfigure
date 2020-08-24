package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public enum AsyncRetryResultEnum {
    /**成功*/
    SUCCESS,
    /**失败*/
    FAIL,
    /**无操作*/
    NOOP;
}
