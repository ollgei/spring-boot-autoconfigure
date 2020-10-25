package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryableSubscriber implements OllgeiDisruptorSubscriber<RetryableSubscription>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private boolean safeMode;

    public RetryableSubscriber(boolean safeMode) {
        this.safeMode = safeMode;
    }

    @Override
    public void onNext(RetryableSubscription subscription) {
        final OllgeiDisruptorService service = applicationContext.getBean(subscription.getClazz());
        if (safeMode) {
            service.safeRead(subscription.getContext(), subscription.getCountDownLatch());
        } else {
            service.read(subscription.getContext(), subscription.getCountDownLatch());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
