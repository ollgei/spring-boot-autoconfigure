package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.ollgei.base.commonj.feign.gson.GsonDecoder;
import com.github.ollgei.base.commonj.feign.gson.GsonEncoder;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import feign.Feign;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "feign.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HttpClientProperties.class)
@Import({ApacheHttpClientConfiguration.class, Okhttp3Configuration.class, HystrixFeignConfiguration.class})
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass("feign.hystrix.HystrixFeign")
    public FeignClientDefination feignClientDefination(
            HttpClientProperties properties,
            ObjectProvider<FeignCustomizer> builderCustomizers) {
        return FeinBuilderHelper.builder(
                properties.getLoggerType(), properties.getFeignLoggerLevel(),
                Feign.builder(), builderCustomizers.orderedStream().collect(Collectors.toList())).
                encoder(new GsonEncoder()).
                decoder(new GsonDecoder(new GsonBuilder().serializeNulls().create())).
                target(FeignClientDefination.class, "http://127.0.0.1:8080");
    }


    @Bean
    @ConditionalOnMissingBean
    public FeignClientManager feignClientManager() {
        return new FeignClientManager();
    }

    @Bean
    @ConditionalOnProperty(prefix = HttpClientProperties.PREFIX,
            name = "retry.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingClass({"feign.okhttp.OkHttpClient", "feign.httpclient.ApacheHttpClient"})
    public FeignCustomizer retryFeignCustomizer(HttpClientProperties properties) {
        return new RetryFeignCustomizer(properties.getRetryNum());
    }

    /**
     * ApacheHttpClient configuration.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ApacheHttpClient.class)
    @ConditionalOnMissingClass("feign.okhttp.OkHttpClient")
    static class ApacheHttpClientConfigure {

        @Bean
        public FeignCustomizer apacheHttpClientFeignCustomizer(ObjectProvider<ApacheHttpClientWrapper> apacheHttpClientWrapperIf) {
            return new ApacheHttpClientFeignCustomizer(apacheHttpClientWrapperIf.getIfUnique());
        }

    }

    /**
     * OkHttpClient configuration.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(OkHttpClient.class)
    static class OkHttpClientConfigure {

        @Bean
        @ConditionalOnClass(name = "feign.okhttp.OkHttpClient")
        public FeignCustomizer okHttp3ClientFeignCustomizer(ObjectProvider<OkHttp3ClientWrapper> okHttp3ClientWrapperIf) {
            return new OkHttp3ClientFeignCustomizer(okHttp3ClientWrapperIf.getIfUnique());
        }

    }

    /**
     * OkHttpClient configuration.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Slf4jLogger.class)
    static class Slf4jLoggerConfigure {

        @Bean
        public FeignCustomizer okHttp3ClientFeignCustomizer(HttpClientProperties properties) {
            return new Slf4jLoggerCustomizer(properties.getLoggerType());
        }

    }

    static class RetryFeignCustomizer implements FeignCustomizer {

        private int retryNum;

        public RetryFeignCustomizer(int retryNum) {
            this.retryNum = retryNum;
        }

        @Override
        public void customize(Feign.Builder builder) {
            builder.retryer(new Retryer.Default(100, SECONDS.toMillis(1), retryNum));
        }
    }

    static class Slf4jLoggerCustomizer implements FeignCustomizer {

        private LoggerType loggerType;

        public Slf4jLoggerCustomizer(LoggerType loggerType) {
            this.loggerType = loggerType;
        }

        @Override
        public void customize(Feign.Builder builder) {
            if (loggerType == LoggerType.SLF4J) {
                builder.logger(new Slf4jLogger());
            }
        }

    }

    static class ApacheHttpClientFeignCustomizer implements FeignCustomizer {

        private ApacheHttpClientWrapper apacheHttpClientWrapper;

        public ApacheHttpClientFeignCustomizer(ApacheHttpClientWrapper wrapper) {
            this.apacheHttpClientWrapper = wrapper;
        }

        @Override
        public void customize(Feign.Builder builder) {
            builder.client(new ApacheHttpClient(apacheHttpClientWrapper.getClient()));
        }
    }

    static class OkHttp3ClientFeignCustomizer implements FeignCustomizer {

        private OkHttp3ClientWrapper okHttp3ClientWrapper;

        public OkHttp3ClientFeignCustomizer(OkHttp3ClientWrapper wrapper) {
            this.okHttp3ClientWrapper = wrapper;
        }

        @Override
        public void customize(Feign.Builder builder) {
            builder.client(new OkHttpClient(okHttp3ClientWrapper.getClient()));
        }
    }
}
