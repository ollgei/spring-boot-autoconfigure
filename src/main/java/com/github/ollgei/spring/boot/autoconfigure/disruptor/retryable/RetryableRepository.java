package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
interface RetryableRepository {

    /**
     * desc.
     * @param context context
     */
    void remove(RetryableContext context);

    /**
     * desc.
     */
    void removeAllExpired();

    /**
     * desc.
     * @param context context
     * @return success
     */
    int readState(RetryableContext context);

}
