package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import java.util.concurrent.CountDownLatch;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableEngine<T> {

    /**
     * desc.
     * @param topic topic
     */
    void publishAndWrite(RetryableKey key, RetryableTopic<T> topic);

    /**
     * desc.
     * @param key key
     */
    void readAndProcess(RetryableKey key);

    /**
     * desc.
     * @param key key
     * @param countDownLatch latch
     */
    default void readAndProcess(RetryableKey key, CountDownLatch countDownLatch) {
        try {
            readAndProcess(key);
        } finally {
            if (countDownLatch.getCount() > 0) {
                countDownLatch.countDown();
            }
        }
    }

}
