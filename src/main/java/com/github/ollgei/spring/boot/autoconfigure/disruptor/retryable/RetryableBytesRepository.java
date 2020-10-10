package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableBytesRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @return success
     */
    byte[] readUpstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    byte[] readMidstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    byte[] readDownstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    byte[] readResponse(RetryableContext context);

    void save(RetryableContext context, RetryableBytesModel model);

    void update(RetryableContext context, RetryableBytesModel model);
}
