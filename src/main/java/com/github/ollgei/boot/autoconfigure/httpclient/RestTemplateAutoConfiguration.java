package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.github.ollgei.boot.autoconfigure.gson.spring.OllgeiGsonHttpMessageConverter;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnProperty(prefix = HttpClientProperties.PREFIX, name = "resttemplate.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HttpClientProperties.class)
@Import({ApacheHttpClientConfiguration.class, Okhttp3Configuration.class})
public class RestTemplateAutoConfiguration {

    @Bean
    @Primary
    public RestTemplate restTemplate(ObjectProvider<ClientHttpRequestFactory> requestFactories) {
        return create(requestFactories.getIfAvailable());
    }

    @Bean(name = "loadbalancer")
    @ConditionalOnMissingBean(name = "loadbalancer")
    @ConditionalOnClass(name = "org.springframework.cloud.client.loadbalancer.LoadBalancerClient")
    @LoadBalanced
    public RestTemplate loadbalancer(ObjectProvider<ClientHttpRequestFactory> requestFactories) {
        return create(requestFactories.getIfAvailable());
    }

    private RestTemplate create(ClientHttpRequestFactory factory) {
        final RestTemplate restTemplate = factory != null ?
                new RestTemplate(factory) : new RestTemplate();
        final List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(3, new OllgeiGsonHttpMessageConverter());
        return restTemplate;
    }

}
