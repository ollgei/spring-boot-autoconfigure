package com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableSubscriber;

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
