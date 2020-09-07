package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableRepository<C extends RetryableContext> {

    void save(RetryableModel model);

    RetryableModel query(C context);

    void update(C context, RetryableModel model);

}
