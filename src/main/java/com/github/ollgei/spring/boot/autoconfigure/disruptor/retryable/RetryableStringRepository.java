package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableStringRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @return success
     */
    String readUpstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    String readMidstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    String readDownstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    String readResponse(RetryableContext context);

    void save(RetryableContext context, RetryableStringModel model);

    void update(RetryableContext context, RetryableStringModel model);
}
