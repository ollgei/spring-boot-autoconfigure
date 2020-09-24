package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableRepository {

    /**
     * desc.
     * @param context context
     * @param model model
     */
    void save(RetryableContext context, RetryableModel model);

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

    /**
     * desc.
     * @param context context
     * @param model model
     */
    void update(RetryableContext context, RetryableModel model);

}
