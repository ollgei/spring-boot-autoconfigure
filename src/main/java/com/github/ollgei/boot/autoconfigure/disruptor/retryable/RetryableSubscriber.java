package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryableSubscriber<T> implements OllgeiDisruptorSubscriber<RetryableSubscription> {

    private RetryableEngine<T> engine;

    public RetryableSubscriber(RetryableEngine<T> engine) {
        this.engine = engine;
    }

    @Override
    public void onNext(RetryableSubscription subscription) {
        if (subscription.getCountDownLatch() != null) {
            engine.readAndProcess(subscription.getKey(), subscription.getCountDownLatch());
            return;
        }
        engine.readAndProcess(subscription.getKey());
    }
}
