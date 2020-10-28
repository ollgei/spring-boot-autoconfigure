package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public abstract class AbstractRetryableEngine<T> implements RetryableEngine<T>, InitializingBean, DisposableBean {
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
        publisher.write(new RetryableSubscription(topic.getServiceName(), key, latch));
        processor.init(model);
    }

    @Override
    public void readAndProcess(String serviceName, RetryableKey key) {
        processor.handle(serviceName, key);
    }

    public abstract EngineType type();

    public void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void destroy() {
        if (publisher != null) {
            publisher.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (publisher != null) {
            publisher.afterPropertiesSet();
        }
    }
}
