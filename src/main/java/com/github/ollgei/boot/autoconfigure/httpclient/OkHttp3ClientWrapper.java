package com.github.ollgei.boot.autoconfigure.httpclient;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
public class OkHttp3ClientWrapper implements DisposableBean {

    private OkHttpClient client;

    /**
     * Create a factory with the given {@link OkHttpClient} instance.
     * @param client the client to use
     */
    public OkHttp3ClientWrapper(OkHttpClient client) {
        Assert.notNull(client, "OkHttpClient must not be null");
        this.client = client;
    }

    @Override
    public void destroy() throws Exception {
        // Clean up the client if we created it in the constructor
        Cache cache = this.client.cache();
        if (cache != null) {
            cache.close();
        }
        this.client.dispatcher().executorService().shutdown();
        this.client.connectionPool().evictAll();
    }

    public OkHttpClient getClient() {
        return client;
    }
}