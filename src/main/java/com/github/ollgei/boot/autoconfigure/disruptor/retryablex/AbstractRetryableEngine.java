package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class AbstractRetryableEngine<T> implements RetryableEngine<T> {
    private OllgeiDisruptorPublisher publisher;
    private RetryableProcessor<T> processor;

    public AbstractRetryableEngine(OllgeiDisruptorPublisher publisher, RetryableProcessor<T> processor) {
        this.publisher = publisher;
        this.processor = processor;
    }

    @Override
    public void publishAndWrite(RetryableKey key, RetryableTopic<T> topic) {
        if (topic instanceof RetryableSyncTopic) {
            publisher.write(new RetryableSubscription(key, ((RetryableSyncTopic) topic).getCountDownLatch()));
        } else {
            publisher.write(new RetryableSubscription(key));
        }
        processor.init(buildModel(key, topic));
    }

    @Override
    public void readAndProcess(RetryableKey key) {
        processor.handle(key);
    }

    private RetryableModel buildModel(RetryableKey key, RetryableTopic<T> topic) {
        final RetryableModel<T> model = new RetryableModel<>();
        if (topic instanceof RetryableSyncTopic) {
            model.setSync(true);
        }
        model.setKey(key.stringizing());
        model.setRequest(topic.getRequest());
        model.setResponse(topic.getResponse());
        return model;
    }
}
