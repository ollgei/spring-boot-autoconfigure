package com.github.ollgei.boot.autoconfigure.httpclient;

import feign.Feign;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public interface FeignCustomizer {

    /**
     * Customize the {@link Feign.Builder}.
     * @param builder the builder to customize
     */
    void customize(Feign.Builder builder);

    default int orderd() {
        return Integer.MAX_VALUE;
    }
}
