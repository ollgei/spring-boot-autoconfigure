package com.github.ollgei.boot.autoconfigure.disruptor.core;

/**
 * Subscriber
 *
 * @author jiawei
 * @since 1.0.0
 */
public interface OllgeiDisruptorSubscriber<T extends AbstractSubscription> {

    /**
     * trigger next.
     * @param subscription subscription
     */
    void onNext(T subscription);
}
