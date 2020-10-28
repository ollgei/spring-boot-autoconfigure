package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

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
    private final String serviceName;
    private final RetryableKey key;
    private final CountDownLatch countDownLatch;

    public RetryableSubscription(String serviceName, RetryableKey key, CountDownLatch countDownLatch) {
        this.serviceName = serviceName;
        this.key = key;
        this.countDownLatch = countDownLatch;
    }

    public RetryableSubscription(String serviceName, RetryableKey key) {
        this(serviceName, key, null);
    }

    public RetryableKey getKey() {
        return key;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public String getServiceName() {
        return serviceName;
    }

}
