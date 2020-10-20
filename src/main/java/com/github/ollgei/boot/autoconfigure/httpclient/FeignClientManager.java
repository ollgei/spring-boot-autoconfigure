package com.github.ollgei.boot.autoconfigure.httpclient;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.utils.CommonHelper;
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

    public String postForText(String uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postS(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public String postForText(String uri, Object body) {
        return postForText(uri, Collections.emptyMap(), body);
    }

    public String getForText(String uri, Map<String, String> headerMap) {
        return feignClientDefination.getS(CommonHelper.newURI(uri), headerMap);
    }

    public String getForText(String uri) {
        return getForText(uri, Collections.emptyMap());
    }

    public JsonElement postForJson(String uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postJ(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public JsonElement getForJson(String uri, Map<String, String> headerMap) {
        return feignClientDefination.getJ(CommonHelper.newURI(uri), headerMap);
    }

    public JsonElement postForJson(String uri, Object body) {
        return postForJson(uri, Collections.emptyMap(), body);
    }

    public JsonElement getForJson(String uri) {
        return getForJson(uri, Collections.emptyMap());
    }

    public <T> T postForJson(String uri, Map<String, String> headerMap, Object body, Class<T> type) {
        return gson.fromJson(postForJson(uri, headerMap, gson.toJsonTree(body)), type);
    }

    public <T> T postForJson(String uri, Object body, Class<T> type) {
        return postForJson(uri, Collections.emptyMap(), body, type);
    }

    public <T> T getForJson(String uri, Map<String, String> headerMap, Class<T> type) {
        return gson.fromJson(getForJson(uri, headerMap), type);
    }

    public <T> T getForJson(String uri, Class<T> type) {
        return getForJson(uri, Collections.emptyMap(), type);
    }

    public Response post(String uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.post(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public Response get(String uri, Map<String, String> headerMap) {
        return feignClientDefination.get(CommonHelper.newURI(uri), headerMap);
    }

    public Response get(String uri) {
        return get(uri, Collections.emptyMap());
    }

    public Response post(String uri, Object body) {
        return post(uri, Collections.emptyMap(), body);
    }

    @Autowired
    public void setFeignClientDefination(FeignClientDefination feignClientDefination) {
        this.feignClientDefination = feignClientDefination;
    }
}
