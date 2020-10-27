package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableSubscriber;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableSubscriber extends RetryableSubscriber<JsonElement> {
    public JsonRetryableSubscriber(RetryableEngine<JsonElement> engine) {
        super(engine);
    }
}
