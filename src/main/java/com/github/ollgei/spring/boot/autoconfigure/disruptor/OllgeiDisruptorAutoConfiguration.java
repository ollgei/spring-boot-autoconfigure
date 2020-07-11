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

package com.github.ollgei.spring.boot.autoconfigure.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * boot-parent.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "ollgei.disruptor", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(OllgeiDisruptorProperties.class)
@ConditionalOnClass(Disruptor.class)
public class OllgeiDisruptorAutoConfiguration {

    private final OllgeiDisruptorProperties properties;

    @Autowired
    public OllgeiDisruptorAutoConfiguration(OllgeiDisruptorProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public OllgeiDisruptorPublisher ollgeiDisruptorPublisher(OllgeiDisruptorSubscriber subscriber) {
        OllgeiDisruptorPublisher.builder().build();
        return OllgeiDisruptorPublisher.builder()
                        .setBufferSize(properties.getBufferSize())
                        .setSubscriberCount(properties.getSubscriberSize())
                        .setSubscriberName(properties.getSubscriberName())
                        .setSubscriber(subscriber)
                        .setGlobalQueue(properties.isGlobalQueue())
                        .setProducerType(properties.isMulti() ?
                                ProducerType.MULTI : ProducerType.SINGLE)
                        .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public OllgeiDisruptorSubscriber ollgeiDisruptorSubscriber() {
        return subscription -> {
            throw new RuntimeException("Not Config Disruptor Subscriber!!");
        };
    }

}
