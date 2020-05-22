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

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.DisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.DisruptorSubscriber;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * boot-parent.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "ollgei.disruptor", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(DisruptorProperties.class)
@ConditionalOnClass(Disruptor.class)
public class DisruptorAutoConfiguration {

    private final DisruptorProperties properties;

    @Autowired
    public DisruptorAutoConfiguration(DisruptorProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DisruptorPublisher disruptorPublisher(DisruptorSubscriber subscriber) {
        DisruptorPublisher.builder().build();
        return DisruptorPublisher.builder()
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
    public DisruptorSubscriber disruptorSubscriber() {
        return data -> {
            throw new RuntimeException("Not Config Disruptor Subscriber!!");
        };
    }

}
