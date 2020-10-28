package com.github.ollgei.boot.autoconfigure.disruptor.retryable.object;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ollgei.boot.autoconfigure.disruptor.RetryableProperties;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableService;
import com.lmax.disruptor.dsl.ProducerType;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RetryableProperties.class)
public class ObjectRetryableConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectRetryableRepository objectRetryableRepository() {
        return new ObjectRetryableMapRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectRetryableProcessor objectRetryableProcessor(ObjectRetryableRepository retryableRepository, ObjectProvider<ObjectRetryableBaseService> retryableServices) {
        final List<RetryableService<Object>> services =
                retryableServices.orderedStream().collect(Collectors.toList());
        return new ObjectRetryableProcessor(retryableRepository, services);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectRetryableEngine objectRetryableEngine(RetryableProperties retryableProperties, ObjectRetryableProcessor objectRetryableProcessor) {
        final ObjectRetryableEngine engine = new ObjectRetryableEngine(objectRetryableProcessor);
        final ObjectRetryableSubscriber subscriber = new ObjectRetryableSubscriber();
        final OllgeiDisruptorPublisher publisher = OllgeiDisruptorPublisher.builder()
                .setBufferSize(retryableProperties.getBufferSize())
                .setSubscriberCount(retryableProperties.getSubscriberSize())
                .setSubscriberName(retryableProperties.getSubscriberName())
                .setGlobalQueue(retryableProperties.isGlobalQueue())
                .setSubscriber(subscriber)
                .setProducerType(retryableProperties.isMulti() ? ProducerType.MULTI : ProducerType.SINGLE)
                .build();
        subscriber.setEngine(engine);
        engine.setPublisher(publisher);
        return engine;
    }
}
