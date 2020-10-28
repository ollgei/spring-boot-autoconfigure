package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.EngineType;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableProcessor;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableEngine extends AbstractRetryableEngine<JsonElement> {

    public JsonRetryableEngine(RetryableProcessor<JsonElement> processor) {
        super(processor);
    }

    @Override
    public EngineType type() {
        return EngineType.JSON;
    }
}
