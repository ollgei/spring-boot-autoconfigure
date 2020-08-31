package com.github.ollgei.spring.boot.autoconfigure.disruptor.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class SpringOllgeiDisruptorSubscriber implements OllgeiDisruptorSubscriber<SpringOllgeiDisruptorSubscription>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void onNext(SpringOllgeiDisruptorSubscription subscription) {
        final OllgeiDisruptorService service = applicationContext.getBean(subscription.getClazz());
        service.read(subscription.getContext());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
