package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.Collections;
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

    private List<RetryableService<T>> services;

    public AbstractRetryableProcessor(List<RetryableService<T>> services) {
        this.services = services;
    }

    @Override
    public void init(String serviceName, RetryableModel<T> model) {
        resolveServices(serviceName).forEach(s -> s.insert(model));
    }

    @Override
    public void handle(String serviceName, RetryableKey key) {
        resolveServices(serviceName).forEach(s -> s.handle(key));
    }

    private List<RetryableService<T>> resolveServices(String serviceName) {
        final List<RetryableService<T>> targets = services.stream().
                filter(s -> s.name().equals(serviceName)).
                collect(Collectors.toList());
        if (targets.size() == 0) {
            log.warn("Not found retryable serviceName[{}]", serviceName);
            return Collections.emptyList();
        }
        return targets;
    }
}
