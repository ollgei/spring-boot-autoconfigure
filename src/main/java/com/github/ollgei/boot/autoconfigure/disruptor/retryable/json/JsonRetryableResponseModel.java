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

    public static RetryableResponseModel<JsonElement> noop() {
        return new JsonRetryableResponseModel(RetryableResultEnum.NOOP);
    }

    public static RetryableResponseModel<JsonElement> success() {
        return new JsonRetryableResponseModel(RetryableResultEnum.SUCCESS);
    }

    public static RetryableResponseModel<JsonElement> fail() {
        return new JsonRetryableResponseModel(RetryableResultEnum.FAIL);
    }

}
