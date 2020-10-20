package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.apache.http.HttpRequest;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.apache.http.client.HttpClient")
@ConditionalOnMissingClass("okhttp3.OkHttpClient")
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "apache",
        havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(ClientHttpRequestFactory.class)
class ApacheHttpClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ApacheHttpClientWrapper httpClient(ObjectProvider<ApacheHttpClientBuilderCustomizer> builderCustomizers) {
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().disableContentCompression().disableCookieManagement();
        builderCustomizers.orderedStream().forEach(customizer -> customizer.customize(httpClientBuilder));
        return new ApacheHttpClientWrapper(httpClientBuilder.build());
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(ApacheHttpClientWrapper apacheHttpClientWrapper) {
        return new HttpComponentsClientHttpRequestFactory(apacheHttpClientWrapper.getClient());
    }

    @Bean
    @ConditionalOnProperty(prefix = HttpClientProperties.PREFIX + ".apache",
            name = "system.enabled", havingValue = "true", matchIfMissing = true)
    public ApacheHttpClientBuilderCustomizer systemHttpClientBuilderCustomizer() {
        return new SystemApacheHttpClientBuilderCustomizer();
    }

    @Bean
    @ConditionalOnProperty(prefix = HttpClientProperties.PREFIX,
            name = "retry.enabled", havingValue = "true", matchIfMissing = true)
    public ApacheHttpClientBuilderCustomizer retryHttpClientBuilderCustomizer(HttpClientProperties properties) {
        return new RetryApacheHttpClientBuilderCustomizer(properties.getRetryNum());
    }

    static class RetryApacheHttpClientBuilderCustomizer implements ApacheHttpClientBuilderCustomizer {

        private int retryNum;

        public RetryApacheHttpClientBuilderCustomizer(int retryNum) {
            this.retryNum = retryNum;
        }

        @Override
        public void customize(HttpClientBuilder builder) {
            builder.setRetryHandler(new OllgeiHttpRequestRetryHandler(retryNum, false));
        }
    }

    static class SystemApacheHttpClientBuilderCustomizer implements ApacheHttpClientBuilderCustomizer {
        @Override
        public void customize(HttpClientBuilder builder) {
            builder.useSystemProperties();
        }
    }

    static class OllgeiHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler {

        private final Map<String, Boolean> idempotentMethods;

        /**
         * Default constructor
         */
        public OllgeiHttpRequestRetryHandler(final int retryCount, final boolean requestSentRetryEnabled) {
            super(retryCount, requestSentRetryEnabled, Arrays.asList(
//                    InterruptedIOException.class,
                    UnknownHostException.class,
                    ConnectException.class,
                    SSLException.class));
            this.idempotentMethods = new ConcurrentHashMap<>();
            this.idempotentMethods.put("GET", Boolean.TRUE);
            this.idempotentMethods.put("HEAD", Boolean.TRUE);
            this.idempotentMethods.put("PUT", Boolean.TRUE);
            this.idempotentMethods.put("DELETE", Boolean.TRUE);
            this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
            this.idempotentMethods.put("TRACE", Boolean.TRUE);
        }

        /**
         * Default constructor
         */
        public OllgeiHttpRequestRetryHandler() {
            this(3, false);
        }

        @Override
        protected boolean handleAsIdempotent(final HttpRequest request) {
            final String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
            final Boolean b = this.idempotentMethods.get(method);
            return b != null && b.booleanValue();
        }
    }
}