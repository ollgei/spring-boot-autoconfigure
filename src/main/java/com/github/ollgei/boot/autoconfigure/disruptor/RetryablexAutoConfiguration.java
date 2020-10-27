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
import com.github.ollgei.boot.autoconfigure.disruptor.retryable.RetryablePublisher;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableRepository;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.RetryableService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json.JsonRetryableBaseService;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json.JsonRetryableEngine;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json.JsonRetryableProcessor;
import com.github.ollgei.boot.autoconfigure.disruptor.retryablex.json.JsonRetryableRepository;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * boot-parent.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "ollgei.retryablex", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(RetryableProperties.class)
@ConditionalOnClass(Disruptor.class)
public class RetryablexAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RetryablePublisher retryablePublisher(RetryableProperties retryableProperties) {
        return new RetryablePublisher(retryableProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public JsonRetryableProcessor jsonRetryableProcessor(ObjectProvider<JsonRetryableRepository> retryableRepositories, ObjectProvider<JsonRetryableBaseService> retryableServices) {
        final RetryableRepository<JsonElement> repository =
                retryableRepositories.getIfAvailable();
        final List<RetryableService<JsonElement>> services =
                retryableServices.orderedStream().collect(Collectors.toList());

        if (repository == null) {
            return new JsonRetryableProcessor(services);
        }
        return new JsonRetryableProcessor(repository, services);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonRetryableEngine jsonRetryableEngine(RetryablePublisher retryablePublisher, JsonRetryableProcessor jsonRetryableProcessor) {
        return new JsonRetryableEngine(retryablePublisher.getPublisher(),
                jsonRetryableProcessor);
    }

}
