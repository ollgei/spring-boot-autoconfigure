package com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json;

import java.util.List;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.AbstractRetryableProcessor;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableService;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class JsonRetryableProcessor extends AbstractRetryableProcessor<JsonElement> {
    public JsonRetryableProcessor(RetryableRepository<JsonElement> repository, List<RetryableService<JsonElement>> retryableServices) {
        super(repository, retryableServices);
    }

    public JsonRetryableProcessor(List<RetryableService<JsonElement>> retryableServices) {
        super(retryableServices);
    }
}
