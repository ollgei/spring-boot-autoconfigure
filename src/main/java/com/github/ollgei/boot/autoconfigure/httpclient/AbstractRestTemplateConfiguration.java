package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.List;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.github.ollgei.boot.autoconfigure.gson.spring.OllgeiGsonHttpMessageConverter;

class AbstractRestTemplateConfiguration {

    protected RestTemplate create(ClientHttpRequestFactory factory) {
        final RestTemplate restTemplate = factory != null ?
                new RestTemplate(factory) : new RestTemplate();
        final List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(3, new OllgeiGsonHttpMessageConverter());
        return restTemplate;
    }
}
