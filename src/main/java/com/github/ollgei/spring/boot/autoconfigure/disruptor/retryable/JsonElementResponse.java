package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.base.commonj.gson.JsonElement;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonElementResponse extends RetryableDownstreamResponse {

    private JsonElement element;

    public JsonElementResponse(JsonElement element) {
        this.element = element;
    }

    public JsonElement getElement() {
        return element;
    }
}
