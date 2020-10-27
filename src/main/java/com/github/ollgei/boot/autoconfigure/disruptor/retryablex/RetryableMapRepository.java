package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class RetryableMapRepository<T> implements RetryableRepository<T> {

    private Map<String, RetryableModel<T>> lruCache =
            new ConcurrentHashMap<>(8);

    @Override
    public RetryableModel<T> query(RetryableKey key) {
        return lruCache.get(key.stringizing());
    }

    @Override
    public void insert(RetryableModel<T> model) {
        lruCache.put(model.getKey(),model);
    }

    @Override
    public void update(RetryableModel<T> model) {
        lruCache.replace(model.getKey(), model);
    }

    @Override
    public void remove(RetryableModel<T> model) {
        lruCache.remove(model.getKey());
    }

    @Override
    public void manual(RetryableModel<T> model) {

    }
}
