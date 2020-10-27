package com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.AbstractRetryableService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableConfiguration;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableMapRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public abstract class JsonRetryableBaseService extends AbstractRetryableService<JsonElement> {
    public JsonRetryableBaseService(RetryableRepository<JsonElement> retryableRepository, RetryableConfiguration retryableConfiguration) {
        super(retryableRepository, retryableConfiguration);
    }

    public JsonRetryableBaseService(RetryableConfiguration retryableConfiguration) {
        this(new RetryableMapRepository<>(), retryableConfiguration);
    }

    public JsonRetryableBaseService() {
        this(new RetryableMapRepository<>(), RetryableConfiguration.builder().build());
    }
}
