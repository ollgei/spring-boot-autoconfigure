package com.github.ollgei.boot.autoconfigure.disruptor.retryable.object;

import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.EngineType;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableProcessor;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class ObjectRetryableEngine extends AbstractRetryableEngine<Object> {

    public ObjectRetryableEngine(RetryableProcessor<Object> processor) {
        super(processor);
    }

    @Override
    public EngineType type() {
        return EngineType.OBJECT;
    }
}
