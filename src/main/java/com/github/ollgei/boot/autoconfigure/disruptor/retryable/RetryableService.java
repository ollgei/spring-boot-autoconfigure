package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableService<T> {

    /**
     * desc.
     * @return name
     */
    String name();


    /**
     * desc.
     * @param model model
     */
    void insert(RetryableModel<T> model);

    /**
     * desc.
     * @param key key
     * @return
     */
    void handle(RetryableKey key);

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    RetryableResponseModel<T> upstream(RetryableModel<T> model);

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    RetryableResponseModel<T> midstream(RetryableModel<T> model);

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    RetryableResponseModel<T> downstream(RetryableModel<T> model);

}
