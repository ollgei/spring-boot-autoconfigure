package com.github.ollgei.boot.autoconfigure.disruptor.retryable.object;

import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableResponseModel;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableResultEnum;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class ObjectRetryableResponseModel extends RetryableResponseModel<Object> {

    public ObjectRetryableResponseModel(RetryableResultEnum result) {
        super(result);
    }

    public static ObjectRetryableResponseModel noop() {
        return new ObjectRetryableResponseModel(RetryableResultEnum.NOOP);
    }

    public static ObjectRetryableResponseModel success() {
        return new ObjectRetryableResponseModel(RetryableResultEnum.SUCCESS);
    }

    public static ObjectRetryableResponseModel fail() {
        return new ObjectRetryableResponseModel(RetryableResultEnum.FAIL);
    }

}
