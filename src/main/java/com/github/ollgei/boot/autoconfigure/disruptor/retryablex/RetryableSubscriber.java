package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryableSubscriber<T> implements OllgeiDisruptorSubscriber<RetryableSubscription> {

    private RetryableEngine<T> retryableEngine;

    public RetryableSubscriber(RetryableEngine<T> retryableEngine) {
        this.retryableEngine = retryableEngine;
    }

    @Override
    public void onNext(RetryableSubscription subscription) {
        if (subscription.getCountDownLatch() != null) {
            retryableEngine.readAndProcess(subscription.getKey(), subscription.getCountDownLatch());
            return;
        }
        retryableEngine.readAndProcess(subscription.getKey());
    }
}
