package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.base.commonj.gson.JsonElement;
import feign.Response;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class FeignClientManager {

    private FeignClientDefination feignClientDefination;

    private Gson gson;

    public FeignClientManager() {
        this(new GsonBuilder().create());
    }

    public FeignClientManager(Gson gson) {
        this.gson = gson;
    }

    public String postForText(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postS(uri, headerMap, gson.toJsonTree(body));
    }

    public String postForText(URI uri, Object body) {
        return postForText(uri, Collections.emptyMap(), body);
    }

    public String getForText(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.getS(uri, headerMap);
    }

    public String getForText(URI uri) {
        return getForText(uri, Collections.emptyMap());
    }

    public JsonElement postForJson(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postJ(uri, headerMap, gson.toJsonTree(body));
    }

    public JsonElement getForJson(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.getJ(uri, headerMap);
    }

    public JsonElement postForJson(URI uri, Object body) {
        return postForJson(uri, Collections.emptyMap(), body);
    }

    public JsonElement getForJson(URI uri) {
        return getForJson(uri, Collections.emptyMap());
    }

    public <T> T postForJson(URI uri, Map<String, String> headerMap, Object body, Class<T> type) {
        return gson.fromJson(postForJson(uri, headerMap, gson.toJsonTree(body)), type);
    }

    public <T> T postForJson(URI uri, Object body, Class<T> type) {
        return postForJson(uri, Collections.emptyMap(), body, type);
    }

    public <T> T getForJson(URI uri, Map<String, String> headerMap, Class<T> type) {
        return gson.fromJson(getForJson(uri, headerMap), type);
    }

    public <T> T getForJson(URI uri, Class<T> type) {
        return getForJson(uri, Collections.emptyMap(), type);
    }

    public Response post(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.post(uri, headerMap, gson.toJsonTree(body));
    }

    public Response get(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.get(uri, headerMap);
    }

    public Response get(URI uri) {
        return get(uri, Collections.emptyMap());
    }

    public Response post(URI uri, Object body) {
        return post(uri, Collections.emptyMap(), body);
    }

    @Autowired
    public void setFeignClientDefination(FeignClientDefination feignClientDefination) {
        this.feignClientDefination = feignClientDefination;
    }
}
