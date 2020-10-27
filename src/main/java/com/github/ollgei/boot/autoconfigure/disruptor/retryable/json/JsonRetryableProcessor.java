package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import java.util.List;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableProcessor;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableService;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableProcessor extends AbstractRetryableProcessor<JsonElement> {
    public JsonRetryableProcessor(RetryableRepository<JsonElement> repository, List<RetryableService<JsonElement>> retryableServices) {
        super(repository, retryableServices);
    }
}
