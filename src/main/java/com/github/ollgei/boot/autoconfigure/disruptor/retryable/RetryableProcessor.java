package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * 处理器.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableProcessor<T> {

    /**
     * desc.
     * @param model model
     */
    void init(RetryableModel<T> model);

    /**
     * desc.
     * @param key
     */
    void handle(RetryableKey key);

}
