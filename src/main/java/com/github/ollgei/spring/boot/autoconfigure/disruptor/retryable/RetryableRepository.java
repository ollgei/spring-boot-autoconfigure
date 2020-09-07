package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableRepository<C extends RetryableContext> {

    void save(RetryableModel model);

    int readState(C context);

    byte[] readUpstreamResponse(C context);

    byte[] readMidstreamResponse(C context);

    byte[] readDownstreamResponse(C context);

    void update(C context, RetryableModel model);

}
