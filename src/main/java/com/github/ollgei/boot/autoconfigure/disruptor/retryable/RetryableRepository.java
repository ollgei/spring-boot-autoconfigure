package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

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

}
