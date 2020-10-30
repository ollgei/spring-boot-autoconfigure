package com.github.ollgei.boot.autoconfigure.httpclient;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * desc.
 *
 * @author ollgei
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "org.springframework.cloud.client.loadbalancer.LoadBalancerClient")
public class RestTemplateLoadbalancerConfiguration extends AbstractRestTemplateConfiguration {

    @Bean(name = "loadbalancer")
    @ConditionalOnMissingBean(name = "loadbalancer")
    @LoadBalanced
    public RestTemplate loadbalancer(ObjectProvider<ClientHttpRequestFactory> requestFactories) {
        return create(requestFactories.getIfAvailable());
    }
}
