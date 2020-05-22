package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * Subscriber
 *
 * @author jiawei
 * @since 1.0.0
 */
public interface DisruptorSubscriber<T extends AbstractSubcription> {

    void onNext(T subcription);
}
