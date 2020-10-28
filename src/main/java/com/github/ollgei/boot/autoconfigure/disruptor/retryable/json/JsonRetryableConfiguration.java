package com.github.ollgei.boot.autoconfigure.disruptor.retryable.json;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.RetryableProperties;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableService;
import com.lmax.disruptor.dsl.ProducerType;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RetryableProperties.class)
public class JsonRetryableConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JsonRetryableRepository jsonRetryableRepository() {
        return new JsonRetryableMapRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonRetryableProcessor jsonRetryableProcessor(JsonRetryableRepository retryableRepository, ObjectProvider<JsonRetryableBaseService> retryableServices) {
        final List<RetryableService<JsonElement>> services =
                retryableServices.orderedStream().collect(Collectors.toList());
        return new JsonRetryableProcessor(retryableRepository, services);
    }


    @Bean
    @ConditionalOnMissingBean
    public JsonRetryableEngine jsonRetryableEngine(RetryableProperties retryableProperties, JsonRetryableProcessor jsonRetryableProcessor) {
        final JsonRetryableEngine engine = new JsonRetryableEngine(jsonRetryableProcessor);
        final JsonRetryableSubscriber subscriber = new JsonRetryableSubscriber();
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
