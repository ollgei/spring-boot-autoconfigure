package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import java.util.concurrent.CountDownLatch;

/**
 * cache.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableObjectCache {

    /**
     * put.
     * @param key key
     * @param countDownLatch countDownLatch
     */
    void putCountDownLatch(String key, CountDownLatch countDownLatch);

    /**
     * take.
     * @param key key
     * @return countDownLatch
     */
    CountDownLatch takeCountDownLatch(String key);

    /**
     * take.
     * @param key key
     */
    void removeCountDownLatch(String key);

}
