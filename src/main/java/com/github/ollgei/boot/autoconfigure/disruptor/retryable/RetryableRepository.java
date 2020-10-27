package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableRepository<T> {
    /**
     * desc.
     * @param key key
     * @return RetryableModel
     */
    RetryableModel<T> query(RetryableKey key);

    /**
     * desc.
     * @param model model
     */
    void insert(RetryableModel<T> model);

    /**
     * desc.
     * @param model model
     */
    void update(RetryableModel<T> model);
    /**
     * desc.
     * @param model model
     */
    void remove(RetryableModel<T> model);

    /**
     * desc.
     * @param model model
     */
    void manual(RetryableModel<T> model);
}
