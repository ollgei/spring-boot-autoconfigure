package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.HystrixFeign;
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
@Import({ApacheHttpClientConfiguration.class, Okhttp3Configuration.class})
public class FeignAutoConfiguration {

    @Autowired
    private HttpClientProperties httpClientProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMissingClass("feign.hystrix.HystrixFeign")
    public FeignClientDefination feignClientDefination(ObjectProvider<FeignCustomizer> builderCustomizers) {
        return builder(Feign.builder(), builderCustomizers.orderedStream().collect(Collectors.toList())).
                encoder(new GsonEncoder()).
                decoder(new GsonDecoder(new GsonBuilder().serializeNulls().create())).
                target(FeignClientDefination.class, "http://127.0.0.1:8080");
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
    public FeignClientDefination feignClientDefinationHystrix(ObjectProvider<FeignCustomizer> builderCustomizers, ObjectProvider<FeignClientDefination> fallback) {
        final HystrixFeign.Builder hystrixBuilder = hystrixBuilder();
        builder(hystrixBuilder, builderCustomizers.orderedStream().collect(Collectors.toList())).
                encoder(new GsonEncoder()).
                decoder(new GsonDecoder(new GsonBuilder().serializeNulls().create()));
        return hystrixBuilder.target(FeignClientDefination.class, "http://127.0.0.1:8080",
                new FeignClientDefination.FactoryFallback(fallback.getIfAvailable()));
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

    @Bean
    @ConditionalOnClass(name = "feign.httpclient.ApacheHttpClient")
    @ConditionalOnMissingClass("feign.okhttp.OkHttpClient")
    public FeignCustomizer apacheHttpClientFeignCustomizer(ApacheHttpClientWrapper apacheHttpClientWrapper) {
        return new ApacheHttpClientFeignCustomizer(apacheHttpClientWrapper);
    }

    @Bean
    @ConditionalOnClass(name = "feign.okhttp.OkHttpClient")
    public FeignCustomizer okHttp3ClientFeignCustomizer(OkHttp3ClientWrapper okHttp3ClientWrapper) {
        return new OkHttp3ClientFeignCustomizer(okHttp3ClientWrapper);
    }

    private HystrixFeign.Builder hystrixBuilder () {
        return HystrixFeign.builder().setterFactory((target, method) -> {
            final String groupKey = httpClientProperties.getHystrix().getGroupKey();
            final String commandKey = httpClientProperties.getHystrix().getCommandKey();
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
        });
    }

    private Feign.Builder builder(Feign.Builder builder, List<FeignCustomizer> customizers) {
        final Logger logger;
        final Logger.Level loggerLevel;
        switch (httpClientProperties.getLoggerType()) {
            case CONSOLE:
                logger = new Logger.ErrorLogger();
                break;
            case SLF4J:
                logger = new Slf4jLogger();
                break;
            default:
                logger = new Logger.NoOpLogger();
        }

        switch (httpClientProperties.getFeignLoggerLevel()) {
            case BASIC:
                loggerLevel = Logger.Level.BASIC;
                break;
            case HEADERS:
                loggerLevel = Logger.Level.HEADERS;
                break;
            case FULL:
                loggerLevel = Logger.Level.FULL;
                break;
            default:
                loggerLevel = Logger.Level.NONE;
        }

        final Feign.Builder custom = builder
                .logLevel(loggerLevel)
                .logger(logger);

        customizers.forEach(customizer -> customizer.customize(builder));

        return custom;
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
