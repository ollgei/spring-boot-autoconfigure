package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * cache.
 * @author ollgei
 * @since 1.0
 */
public class DefaultRetryableObjectCache implements RetryableObjectCache {

    private ConcurrentMap<String, Object> caches = new ConcurrentHashMap<>(16);

    @Override
    public void putCountDownLatch(String key, CountDownLatch countDownLatch) {
        caches.putIfAbsent(key, countDownLatch);
    }

    @Override
    public CountDownLatch takeCountDownLatch(String key) {
        return (CountDownLatch) caches.get(key);
    }

    @Override
    public void removeCountDownLatch(String key) {
        if (caches.containsKey(key)) {
            caches.remove(key);
        }
    }
}
