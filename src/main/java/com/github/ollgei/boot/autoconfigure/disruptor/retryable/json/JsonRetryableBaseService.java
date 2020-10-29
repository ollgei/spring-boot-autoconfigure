package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableConfiguration;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableModel;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.object.ObjectRetryableMapRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public abstract class JsonRetryableBaseService extends AbstractRetryableService<JsonElement> {
    public JsonRetryableBaseService(RetryableRepository<JsonElement> retryableRepository, RetryableConfiguration retryableConfiguration) {
        super(retryableRepository, retryableConfiguration);
    }

    public JsonRetryableBaseService(RetryableRepository<JsonElement> retryableRepository) {
        this(retryableRepository, RetryableConfiguration.builder().build());
    }

    public JsonRetryableBaseService() {
        this(new JsonRetryableMapRepository(), RetryableConfiguration.builder().build());
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public JsonRetryableResponseModel upstream(RetryableModel<JsonElement> model) {
        return JsonRetryableResponseModel.noop();
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public JsonRetryableResponseModel midstream(RetryableModel<JsonElement> model) {
        return JsonRetryableResponseModel.noop();
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public JsonRetryableResponseModel downstream(RetryableModel<JsonElement> model) {
        return JsonRetryableResponseModel.noop();
    }
}
