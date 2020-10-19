package com.github.ollgei.boot.autoconfigure.resttemplate;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.Assert;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "okhttp3.OkHttpClient")
@ConditionalOnMissingBean(ClientHttpRequestFactory.class)
@ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX, name = "okhttp3",
        havingValue = "true", matchIfMissing = true)
class Okhttp3Configuration {

    @Bean
    public ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(OkHttp3ClientWrapper okHttp3ClientWrapper) {
        return new OkHttp3ClientHttpRequestFactory(okHttp3ClientWrapper.getClient());
    }

    @Bean(destroyMethod = "destroy")
    public OkHttp3ClientWrapper okHttp3ClientWrapper(ObjectProvider<OkHttp3ClientCustomizer> builderCustomizers) {
        final OkHttp3ClientWrapper wrapper = new OkHttp3ClientWrapper(new OkHttpClient());
        builderCustomizers.orderedStream().forEach(customizer -> customizer.customize(wrapper.getClient()));
        return wrapper;
    }

    static class OkHttp3ClientWrapper implements DisposableBean {

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

}