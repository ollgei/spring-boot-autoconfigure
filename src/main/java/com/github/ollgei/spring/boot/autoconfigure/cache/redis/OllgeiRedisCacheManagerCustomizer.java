package com.github.ollgei.spring.boot.autoconfigure.cache.redis;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;

import com.github.ollgei.spring.boot.autoconfigure.cache.OllgeiCacheProperties;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class OllgeiRedisCacheManagerCustomizer implements RedisCacheManagerBuilderCustomizer {

    private OllgeiCacheProperties cacheProperties;

    public OllgeiRedisCacheManagerCustomizer(OllgeiCacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Override
    public void customize(RedisCacheManagerBuilder builder) {

        final String defaultName = "default";
        builder.initialCacheNames(new HashSet<>(Arrays.asList(defaultName)));
        RedisCacheConfiguration defaultConfig = builder.getCacheConfigurationFor(defaultName).get();

        cacheProperties.getRedis().forEach((name, options) -> {
            RedisCacheConfiguration config = defaultConfig.entryTtl(options.getTimeToLive());
            if (!options.isCacheNullValues()) {
                config = config.disableCachingNullValues();
            }
            builder.withCacheConfiguration(name, config);
        });
    }
}