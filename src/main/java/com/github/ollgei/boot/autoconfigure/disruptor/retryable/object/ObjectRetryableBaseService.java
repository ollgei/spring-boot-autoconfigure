package com.github.ollgei.boot.autoconfigure.disruptor.retryable.object;

import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableConfiguration;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableModel;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public abstract class ObjectRetryableBaseService extends AbstractRetryableService<Object> {
    public ObjectRetryableBaseService(RetryableRepository<Object> retryableRepository, RetryableConfiguration retryableConfiguration) {
        super(retryableRepository, retryableConfiguration);
    }

    public ObjectRetryableBaseService(RetryableRepository<Object> retryableRepository) {
        this(retryableRepository, RetryableConfiguration.builder().build());
    }

    public ObjectRetryableBaseService() {
        this(new ObjectRetryableMapRepository(), RetryableConfiguration.builder().build());
    }


    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public ObjectRetryableResponseModel upstream(RetryableModel<Object> model) {
        return ObjectRetryableResponseModel.noop();
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public ObjectRetryableResponseModel midstream(RetryableModel<Object> model) {
        return ObjectRetryableResponseModel.noop();
    }

    /**
     * desc.
     * @param model model
     * @return RetryableResponseModel
     */
    @Override
    public ObjectRetryableResponseModel downstream(RetryableModel<Object> model) {
        return ObjectRetryableResponseModel.noop();
    }
}
