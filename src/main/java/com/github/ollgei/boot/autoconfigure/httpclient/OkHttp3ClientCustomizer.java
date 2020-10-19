package com.github.ollgei.boot.autoconfigure.httpclient;

import okhttp3.OkHttpClient;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@FunctionalInterface
public interface OkHttp3ClientCustomizer {

    /**
     * Customize the {@link OkHttpClient}.
     * @param httpClient the builder to customize
     */
    void customize(OkHttpClient httpClient);

}
