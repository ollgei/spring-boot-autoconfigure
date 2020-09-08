package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableRepository {

    void save(RetryableModel model);

    int readState(RetryableContext context);

    byte[] readUpstreamResponse(RetryableContext context);

    byte[] readMidstreamResponse(RetryableContext context);

    byte[] readDownstreamResponse(RetryableContext context);

    void update(RetryableContext context, RetryableModel model);

}
