package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * Subscriber
 *
 * @author jiawei
 * @since 1.0.0
 */
public interface DisruptorSubscriber<T extends AbstractSubscription> {

    /**
     * trigger next.
     * @param subscription subscription
     * @return
     */
    void onNext(T subscription);
}
