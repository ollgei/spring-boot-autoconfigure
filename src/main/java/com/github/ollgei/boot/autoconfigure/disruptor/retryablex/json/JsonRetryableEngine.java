package com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.AbstractRetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableProcessor;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableEngine extends AbstractRetryableEngine<JsonElement> {

    public JsonRetryableEngine(OllgeiDisruptorPublisher publisher, RetryableProcessor<JsonElement> processor) {
        super(publisher, processor);
    }
}
