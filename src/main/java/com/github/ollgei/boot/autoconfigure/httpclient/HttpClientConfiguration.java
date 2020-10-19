package com.github.ollgei.boot.autoconfigure.httpclient;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.apache.http.client.HttpClient")
@ConditionalOnMissingClass("okhttp3.OkHttpClient")
@ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX, name = "httpclient",
        havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(ClientHttpRequestFactory.class)
class HttpClientConfiguration {

    @Bean
    public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory(ObjectProvider<HttpClientBuilderCustomizer> builderCustomizers) {
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().disableContentCompression().disableCookieManagement();
        builderCustomizers.orderedStream().forEach(customizer -> customizer.customize(httpClientBuilder));
        return new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
    }

    @Bean
    @ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX + ".httpclient",
            name = "customizer.system.enabled", havingValue = "true", matchIfMissing = true)
    public HttpClientBuilderCustomizer systemHttpClientBuilderCustomizer() {
        return new SystemHttpClientBuilderCustomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX + ".httpclient",
            name = "customizer.retry.enabled", havingValue = "true", matchIfMissing = true)
    public HttpClientBuilderCustomizer retryHttpClientBuilderCustomizer() {
        return new RetryHttpClientBuilderCustomizer();
    }

    static class RetryHttpClientBuilderCustomizer implements HttpClientBuilderCustomizer {
        @Override
        public void customize(HttpClientBuilder builder) {
            builder.setRetryHandler(new DefaultHttpRequestRetryHandler());
        }
    }

    static class SystemHttpClientBuilderCustomizer implements HttpClientBuilderCustomizer {
        @Override
        public void customize(HttpClientBuilder builder) {
            builder.useSystemProperties();
        }
    }
}