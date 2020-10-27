package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import java.util.concurrent.CountDownLatch;

import com.github.ollgei.boot.autoconfigure.disruptor.core.AbstractSubscription;
import lombok.ToString;

/**
 * subscription.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ToString(callSuper = true)
public class RetryableSubscription extends AbstractSubscription {
    private final RetryableKey key;
    private final CountDownLatch countDownLatch;

    public RetryableSubscription(RetryableKey key, CountDownLatch countDownLatch) {
        this.key = key;
        this.countDownLatch = countDownLatch;
    }

    public RetryableSubscription(RetryableKey key) {
        this(key, null);
    }

    public RetryableKey getKey() {
        return key;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
}
