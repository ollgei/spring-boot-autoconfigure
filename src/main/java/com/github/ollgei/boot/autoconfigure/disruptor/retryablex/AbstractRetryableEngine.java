package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import java.util.concurrent.CountDownLatch;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class AbstractRetryableEngine<T> implements RetryableEngine<T> {
    private OllgeiDisruptorPublisher publisher;
    private RetryableProcessor<T> processor;

    public AbstractRetryableEngine(RetryableProcessor<T> processor) {
        this.processor = processor;
    }

    @Override
    public void publishAndWrite(RetryableKey key, RetryableTopic<T> topic) {
        final RetryableModel<T> model = new RetryableModel<>();
        model.setState(0);
        model.setNextRetryMills(System.currentTimeMillis());
        model.setRetryCount(0);
        model.setKey(key.stringizing());
        model.setRequest(topic.getRequest());
        model.setResponse(topic.getResponse());
        final CountDownLatch latch = topic.getLatch();
        if (latch != null) {
            model.setSync(true);
        } else {
            model.setSync(false);
        }
        publisher.write(new RetryableSubscription(key, latch));
        processor.init(model);
    }

    @Override
    public void readAndProcess(RetryableKey key) {
        processor.handle(key);
    }

    public void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
    }
}
