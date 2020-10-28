package com.github.ollgei.boot.autoconfigure.disruptor.retryable.object;

import java.util.List;

import com.github.ollgei.boot.autoconfigure.disruptor.retryable.AbstractRetryableProcessor;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableService;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class ObjectRetryableProcessor extends AbstractRetryableProcessor<Object> {
    public ObjectRetryableProcessor(RetryableRepository<Object> repository, List<RetryableService<Object>> retryableServices) {
        super(repository, retryableServices);
    }
}
