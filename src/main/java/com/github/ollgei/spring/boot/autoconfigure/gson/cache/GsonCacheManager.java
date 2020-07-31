package com.github.ollgei.spring.boot.autoconfigure.gson.cache;

import com.github.ollgei.base.commonj.gson.GsonBuilder;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface GsonCacheManager {

    String DEFAULT_NAME = "default";

    /**
     * desc.
     * @return
     */
    default GsonCache get() {
        return get(DEFAULT_NAME);
    }

    /**
     * desc.
     * @param name name
     * @return
     */
    default GsonCache get(String name) {
        return get(name, new GsonBuilder().disableHtmlEscaping());
    }

    /**
     * desc.
     * @param name name
     * @param builder builder
     * @return
     */
    GsonCache get(String name, GsonBuilder builder);
}
