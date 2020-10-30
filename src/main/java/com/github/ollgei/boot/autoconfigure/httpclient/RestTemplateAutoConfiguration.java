package com.github.ollgei.boot.autoconfigure.httpclient;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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
@Import({ApacheHttpClientConfiguration.class, Okhttp3Configuration.class, RestTemplateLoadbalancerConfiguration.class})
public class RestTemplateAutoConfiguration extends AbstractRestTemplateConfiguration {

    @Bean
    @Primary
    public RestTemplate restTemplate(ObjectProvider<ClientHttpRequestFactory> requestFactories) {
        return create(requestFactories.getIfAvailable());
    }

}
