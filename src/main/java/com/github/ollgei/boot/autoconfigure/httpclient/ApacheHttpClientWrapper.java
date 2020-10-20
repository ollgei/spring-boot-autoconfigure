package com.github.ollgei.boot.autoconfigure.httpclient;

import org.springframework.util.Assert;

import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
public class ApacheHttpClientWrapper {

    private HttpClient client;

    /**
     * Create a factory with the given {@link OkHttpClient} instance.
     * @param client the client to use
     */
    public ApacheHttpClientWrapper(HttpClient client) {
        Assert.notNull(client, "HttpClient must not be null");
        this.client = client;
    }

    public HttpClient getClient() {
        return client;
    }
}