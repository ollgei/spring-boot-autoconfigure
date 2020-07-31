package com.github.ollgei.spring.boot.autoconfigure.gson.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.ollgei.base.commonj.gson.GsonBuilder;

/**
 * simple.
 * @author ollgei
 * @since 1.0.0
 */
public class SimpleGsonCacheManager implements GsonCacheManager {

    private Map<String, GsonCache> cached = new ConcurrentHashMap<>(8);

    private GsonCacheProperties gsonProperties;

    public SimpleGsonCacheManager(final GsonCacheProperties gsonProperties) {
        this.gsonProperties = gsonProperties;
    }

    @Override
    public GsonCache get(String name, GsonBuilder builder) {
        if (cached.containsKey(name)) {
            return cached.get(name);
        }
        return cached.computeIfAbsent(name, k -> new GsonCache(builder));
    }

}
