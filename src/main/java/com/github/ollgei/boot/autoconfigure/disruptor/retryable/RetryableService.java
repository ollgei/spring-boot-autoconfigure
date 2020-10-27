package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableService<T> {

    /**
     * desc.
     * @return key
     */
    String key();

    /**
     * desc.
     * @param model model
     * @return
     */
    void handle(RetryableModel<T> model);

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    default RetryableResponseModel<T> upstream(RetryableModel<T> model) {
        return new RetryableResponseModel<>(RetryableResultEnum.NOOP);
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    default RetryableResponseModel<T> midstream(RetryableModel<T> model) {
        return new RetryableResponseModel<>(RetryableResultEnum.NOOP);
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    default RetryableResponseModel<T> downstream(RetryableModel<T> model) {
        return new RetryableResponseModel<>(RetryableResultEnum.NOOP);
    }

}
