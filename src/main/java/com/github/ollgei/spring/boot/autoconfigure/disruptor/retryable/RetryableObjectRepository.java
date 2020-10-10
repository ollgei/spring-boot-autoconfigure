package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableObjectRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @return success
     */
    Object readUpstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    Object readMidstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    Object readDownstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return success
     */
    Object readResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void save(RetryableContext context, RetryableObjectModel model);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void update(RetryableContext context, RetryableObjectModel model);
}
