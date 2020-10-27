package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableResponseModel;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableResultEnum;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableResponseModel extends RetryableResponseModel<JsonElement> {

    public JsonRetryableResponseModel(RetryableResultEnum result) {
        super(result);
    }

    public static JsonRetryableResponseModel noop() {
        return new JsonRetryableResponseModel(RetryableResultEnum.NOOP);
    }

    public static JsonRetryableResponseModel success() {
        return new JsonRetryableResponseModel(RetryableResultEnum.SUCCESS);
    }

    public static JsonRetryableResponseModel fail() {
        return new JsonRetryableResponseModel(RetryableResultEnum.FAIL);
    }

}
