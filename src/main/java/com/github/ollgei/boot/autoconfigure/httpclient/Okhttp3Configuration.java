package com.github.ollgei.boot.autoconfigure.httpclient;

import java.io.IOException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "okhttp3.OkHttpClient")
@ConditionalOnMissingBean(ClientHttpRequestFactory.class)
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "okhttp3",
        havingValue = "true", matchIfMissing = true)
class Okhttp3Configuration {

    @Bean
    public ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(OkHttp3ClientWrapper okHttp3ClientWrapper) {
        return new OkHttp3ClientHttpRequestFactory(okHttp3ClientWrapper.getClient());
    }

    @Bean(destroyMethod = "destroy")
    public OkHttp3ClientWrapper okHttp3ClientWrapper(ObjectProvider<OkHttp3ClientCustomizer> builderCustomizers) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builderCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
        final OkHttp3ClientWrapper wrapper = new OkHttp3ClientWrapper(builder.build());
        return wrapper;
    }

    @Bean
    @ConditionalOnProperty(prefix = HttpClientProperties.PREFIX,
            name = "retry.enabled", havingValue = "true", matchIfMissing = true)
    public OkHttp3ClientCustomizer retryOkHttp3ClientCustomizer(HttpClientProperties properties) {
        return new RetryOkHttp3ClientCustomizer(properties.getRetryNum());
    }

    static class RetryOkHttp3ClientCustomizer implements OkHttp3ClientCustomizer {
        private int retryNum;

        public RetryOkHttp3ClientCustomizer(int retryNum) {
            this.retryNum = retryNum;
        }

        @Override
        public void customize(OkHttpClient.Builder builder) {
            builder.addInterceptor(new RetryInterceptor(retryNum));
        }
    }

    static class RetryInterceptor implements Interceptor {

        //最大重试次数
        public int maxRetry;
        //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
        private int retryNum = 0;

        public RetryInterceptor(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryNum < maxRetry) {
                retryNum++;
                response = chain.proceed(request);
            }
            return response;
        }
    }

}