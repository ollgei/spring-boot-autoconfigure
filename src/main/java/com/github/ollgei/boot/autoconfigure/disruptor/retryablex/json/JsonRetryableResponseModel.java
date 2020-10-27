package com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableResponseModel;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableResultEnum;

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
