package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ollgei.base.commonj.feign.gson.GsonDecoder;
import com.github.ollgei.base.commonj.feign.gson.GsonEncoder;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.utils.CommonHelper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import feign.Response;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(HystrixFeign.class)
class HystrixFeignConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
    public FeignClientDefination feignClientDefinationHystrix(HttpClientProperties properties,
                                                              ObjectProvider<FeignCustomizer> builderCustomizers,
                                                              ObjectProvider<FeignClientDefination> fallback) {
        final HystrixFeign.Builder hystrixBuilder = HystrixFeign.builder().setterFactory((target, method) -> {
            final String groupKey = properties.getHystrix().getGroupKey();
            final String commandKey = properties.getHystrix().getCommandKey();
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
        });
        FeinBuilderHelper.builder(
                properties.getLoggerType(), properties.getFeignLoggerLevel(),
                hystrixBuilder, builderCustomizers.orderedStream().collect(Collectors.toList())).
                encoder(new GsonEncoder()).
                decoder(new GsonDecoder(new GsonBuilder().serializeNulls().create()));
        return hystrixBuilder.target(FeignClientDefination.class, "http://127.0.0.1:8080",
                new FeignClientFactoryFallback(fallback.getIfAvailable()));
    }

    @Slf4j
    static class FeignClientFallback implements FeignClientDefination {

        @Override
        public Response post(URI uri, Map<String, String> headerMap, JsonElement body) {
            log("POST", uri);
            return null;
        }

        @Override
        public Response post(URI uri, JsonElement body) {
            log("POST",uri);
            return null;
        }

        @Override
        public Response get(URI uri, Map<String, String> headerMap) {
            log("GET", uri);
            return null;
        }

        @Override
        public Response get(URI uri) {
            log("GET", uri);
            return null;
        }

        @Override
        public JsonElement postJ(URI uri, Map<String, String> headerMap, JsonElement body) {
            log("POST",uri);
            return null;
        }

        @Override
        public JsonElement postJ(URI uri, JsonElement body) {
            log("POST",uri);
            return null;
        }

        @Override
        public JsonElement getJ(URI uri, Map<String, String> headerMap) {
            log("GET", uri);
            return null;
        }

        @Override
        public JsonElement getJ(URI uri) {
            log("GET", uri);
            return null;
        }

        @Override
        public String postS(URI uri, Map<String, String> headerMap, JsonElement body) {
            log("POST", uri);
            return null;
        }

        @Override
        public String postS(URI uri, JsonElement body) {
            log("POST", uri);
            return null;
        }

        @Override
        public String getS(URI uri, Map<String, String> headerMap) {
            log("GET", uri);
            return null;
        }

        @Override
        public String getS(URI uri) {
            log("GET", uri);
            return null;
        }

        private void log(String method, URI uri) {
            log.warn("METHOD: {}, URI: {}", method, uri.toString());
        }
    }

    @Slf4j
    static class FeignClientFactoryFallback implements FallbackFactory<FeignClientDefination> {

        private FeignClientDefination fallback;

        public FeignClientFactoryFallback() {
            this(new FeignClientFallback());
        }

        public FeignClientFactoryFallback(FeignClientDefination defination) {
            this.fallback = defination == null ? new FeignClientFallback() : defination;
        }

        @Override
        public FeignClientDefination create(Throwable cause) {
            log.warn("失败原因:{}", CommonHelper.throwableToString(cause));
            return this.fallback;
        }
    }
}
