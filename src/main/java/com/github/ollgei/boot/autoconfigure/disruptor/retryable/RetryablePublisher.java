package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.boot.autoconfigure.disruptor.RetryableProperties;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * RetryablePublisher.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryablePublisher {

    private OllgeiDisruptorPublisher publisher;

    public RetryablePublisher(RetryableProperties properties) {
        this.publisher = OllgeiDisruptorPublisher.builder()
                .setBufferSize(properties.getBufferSize())
                .setSubscriberCount(properties.getSubscriberSize())
                .setSubscriberName(properties.getSubscriberName())
                .setSubscriber(new RetryableSubscriber(properties.isSafeMode()))
                .setGlobalQueue(properties.isGlobalQueue())
                .setProducerType(properties.isMulti() ?
                        ProducerType.MULTI : ProducerType.SINGLE)
                .build();
    }

    public OllgeiDisruptorPublisher getPublisher() {
        return publisher;
    }
}
