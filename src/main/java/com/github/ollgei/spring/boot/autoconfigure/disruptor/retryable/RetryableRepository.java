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
     * @return
     */
    void save(RetryableContext context, RetryableModel model);

    /**
     * desc.
     * @param context context
     * @return
     */
    void remove(RetryableContext context);

    /**
     * desc.
     * @return
     */
    void removeAllExpired();

    /**
     * desc.
     * @param context context
     * @return
     */
    int readState(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return
     */
    byte[] readUpstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return
     */
    byte[] readMidstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return
     */
    byte[] readDownstreamResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @return
     */
    byte[] readResponse(RetryableContext context);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void update(RetryableContext context, RetryableModel model);

}
