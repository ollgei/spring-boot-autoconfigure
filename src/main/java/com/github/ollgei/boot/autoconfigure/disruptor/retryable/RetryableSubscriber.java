package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.List;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryableSubscriber<T> implements OllgeiDisruptorSubscriber<RetryableSubscription> {

    private RetryableEngine<T> engine;

    @Override
    public void onNext(RetryableSubscription subscription) {
        if (subscription.getCountDownLatch() != null) {
            engine.readAndProcess(subscription.getServiceName(), subscription.getKey(), subscription.getCountDownLatch());
            return;
        }
        engine.readAndProcess(subscription.getServiceName(), subscription.getKey());
    }

    public void setEngine(RetryableEngine<T> engine) {
        this.engine = engine;
    }
}
