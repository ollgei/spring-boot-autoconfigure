/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ollgei.boot.autoconfigure.redis;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

import com.github.ollgei.boot.autoconfigure.shedlock.OllgeiShedlockProperties;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Gson.
 *
 * @author David Liu
 * @author Ivan Golovko
 * @since 1.2.0
 */
@ConditionalOnProperty(prefix = OllgeiShedlockProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(OllgeiRedisProperties.class)
@AutoConfigureAfter({ RedisAutoConfiguration.class })
public class OllgeiRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BytesRedisTemplate bytesRedisTemplate(ObjectProvider<RedisConnectionFactory> redisConnectionFactoryIf) {
        return new BytesRedisTemplate(redisConnectionFactoryIf.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean
    public StringBytesRedisTemplate stringBytesRedisTemplate(ObjectProvider<RedisConnectionFactory> redisConnectionFactoryIf) {
        return new StringBytesRedisTemplate(redisConnectionFactoryIf.getIfAvailable());
    }

}
