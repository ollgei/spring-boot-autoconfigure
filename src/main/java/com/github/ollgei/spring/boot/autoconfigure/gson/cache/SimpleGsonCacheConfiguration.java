package com.github.ollgei.spring.boot.autoconfigure.gson.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(GsonCacheManager.class)
@Conditional(GsonCacheCondition.class)
class SimpleGsonCacheConfiguration {

    @Bean
    @SuppressWarnings("all")
    SimpleGsonCacheManager simpleGsonCacheManager(GsonCacheProperties cacheGsonProperties) {
        return new SimpleGsonCacheManager(cacheGsonProperties);
    }

}
