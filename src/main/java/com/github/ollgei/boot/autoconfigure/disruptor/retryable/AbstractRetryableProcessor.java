package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理器.
 * @author ollgei
 * @since 1.0
 */
@Slf4j
public class AbstractRetryableProcessor<T> implements RetryableProcessor<T> {

    private RetryableRepository<T> repository;

    private List<RetryableService<T>> services;

    public AbstractRetryableProcessor(RetryableRepository<T> repository, List<RetryableService<T>> services) {
        this.repository = repository;
        this.services = services;
    }

    @Override
    public void init(RetryableModel<T> model) {
        repository.insert(model);
    }

    @Override
    public void handle(String serviceName, RetryableKey key) {
        final RetryableModel<T> model = repository.query(key);
        final List<RetryableService<T>> targets = services.stream().
                filter(s -> s.name().equals(serviceName)).
                collect(Collectors.toList());
        if (targets.size() == 0) {
            log.warn("Not found retryable serviceName[{}]", serviceName);
            return;
        }
        targets.forEach(s -> s.handle(model));
    }
}
