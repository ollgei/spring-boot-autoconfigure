package com.github.ollgei.boot.autoconfigure.cache;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.ollgei.boot.autoconfigure.cache.redis.OllgeiRedisCacheManagerConfiguration;

/**
 * config.
 * @author ollgei
 * @since 1.0.0
 */
@EnableConfigurationProperties(OllgeiCacheProperties.class)
@Configuration(proxyBeanMethods = false)
@Import(
        OllgeiRedisCacheManagerConfiguration.class
)
public class OllgeiCacheAutoConfiguration {
}
