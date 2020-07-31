package com.github.ollgei.spring.boot.autoconfigure.gson.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ Caffeine.class })
@ConditionalOnMissingBean(GsonCacheManager.class)
@Conditional(GsonCacheCondition.class)
class CaffeineGsonCacheConfiguration {

    @Bean
    @SuppressWarnings("all")
    CaffeineGsonCacheManager caffeineGsonCacheManager(GsonCacheProperties cacheGsonProperties) {
        return new CaffeineGsonCacheManager(cacheGsonProperties);
    }

}
