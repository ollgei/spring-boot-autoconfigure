package com.github.ollgei.spring.boot.autoconfigure.cache.redis;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

import com.github.ollgei.spring.boot.autoconfigure.cache.OllgeiCacheProperties;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedisCacheManager.class, RedisCacheManagerBuilderCustomizer.class })
public class OllgeiRedisCacheManagerConfiguration {

    @Bean
    @SuppressWarnings("all")
    public RedisCacheManagerBuilderCustomizer ollgeiRedisCacheManagerCustomizer(OllgeiCacheProperties properties) {
        return new OllgeiRedisCacheManagerCustomizer(properties);
    }

}
