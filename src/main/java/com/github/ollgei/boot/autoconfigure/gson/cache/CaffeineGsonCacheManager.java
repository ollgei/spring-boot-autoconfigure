package com.github.ollgei.boot.autoconfigure.gson.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * simple.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class CaffeineGsonCacheManager implements GsonCacheManager {

    private Cache<String, GsonCache> cached;

    private GsonCacheProperties gsonProperties;

    public CaffeineGsonCacheManager(final GsonCacheProperties gsonProperties) {
        this.gsonProperties = gsonProperties;
        createCacheConfig(gsonProperties);
    }

    @Override
    public GsonCache get(String name, GsonBuilder builder) {
        return cached.get(name, key -> new GsonCache(builder));
    }

    private void createCacheConfig(GsonCacheProperties gsonProperties) {
        this.cached = Caffeine.newBuilder().
                expireAfter(new Expiry<String, GsonCache>() {
                    @Override
                    public long expireAfterCreate(@NonNull String key, @NonNull GsonCache value, long currentTime) {
                        if (log.isInfoEnabled()) {
                            log.info("{} 不存在或者已经过期，重新加载 {}", key, gsonProperties.getExpire());
                        }
                        return gsonProperties.getExpire();
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull String key, @NonNull GsonCache value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(@NonNull String key, @NonNull GsonCache value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                }).
                maximumSize(gsonProperties.getMaxSize()).
                build();
    }

}
