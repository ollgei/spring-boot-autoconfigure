/*
 * Copyright 2017-2019 Lemonframework Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.ollgei.boot.autoconfigure.disruptor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryableService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableBaseService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableMapRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableProcessor;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.json.JsonRetryableSubscriber;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * boot-parent.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = RetryableProperties.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(RetryableProperties.class)
@ConditionalOnClass(Disruptor.class)
public class RetryableAutoConfiguration {

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
        final OllgeiDisruptorPublisher publisher = OllgeiDisruptorPublisher.builder()
                .setBufferSize(retryableProperties.getBufferSize())
                .setSubscriberCount(retryableProperties.getSubscriberSize())
                .setSubscriberName(retryableProperties.getSubscriberName())
                .setSubscriber(new JsonRetryableSubscriber(engine))
                .setGlobalQueue(retryableProperties.isGlobalQueue())
                .setProducerType(retryableProperties.isMulti() ?
                        ProducerType.MULTI : ProducerType.SINGLE)
                .build();
        engine.setPublisher(publisher);
        return engine;
    }

}
