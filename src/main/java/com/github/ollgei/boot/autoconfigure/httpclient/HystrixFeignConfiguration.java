package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ollgei.base.commonj.feign.gson.GsonDecoder;
import com.github.ollgei.base.commonj.feign.gson.GsonEncoder;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import feign.hystrix.HystrixFeign;

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
                new FeignClientDefination.FactoryFallback(fallback.getIfAvailable()));
    }
}
