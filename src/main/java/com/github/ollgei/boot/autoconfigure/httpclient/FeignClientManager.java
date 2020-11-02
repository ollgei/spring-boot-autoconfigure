package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import com.github.ollgei.base.commonj.enums.HttpMethod;
import com.github.ollgei.base.commonj.errors.ErrorCodeEnum;
import com.github.ollgei.base.commonj.errors.ErrorException;
import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.model.UriWithMethod;
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

    public FeignClientManager(FeignClientDefination feignClientDefination) {
        this(feignClientDefination, new GsonBuilder().create());
    }

    public FeignClientManager(FeignClientDefination feignClientDefination, Gson gson) {
        this.gson = gson;
        this.feignClientDefination = feignClientDefination;
    }

    public String postForText(String uri, Map<String, String> headerMap, Object body) {
        return postForText(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public String postForText(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postS(uri, headerMap, gson.toJsonTree(body));
    }

    public String postForText(String uri, Object body) {
        return postForText(uri, Collections.emptyMap(), body);
    }

    public String postForText(URI uri, Object body) {
        return postForText(uri, Collections.emptyMap(), body);
    }

    public String getForText(String uri, Map<String, String> headerMap) {
        return getForText(CommonHelper.newURI(uri), headerMap);
    }

    public String getForText(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.getS(uri, headerMap);
    }

    public String getForText(String uri) {
        return getForText(uri, Collections.emptyMap());
    }

    public String getForText(URI uri) {
        return getForText(uri, Collections.emptyMap());
    }

    public JsonElement postForJson(String uri, Map<String, String> headerMap, Object body) {
        return postForJson(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public JsonElement postForJson(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.postJ(uri, headerMap, gson.toJsonTree(body));
    }

    public JsonElement getForJson(String uri, Map<String, String> headerMap) {
        return getForJson(CommonHelper.newURI(uri), headerMap);
    }

    public JsonElement getForJson(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.getJ(uri, headerMap);
    }

    public JsonElement postForJson(String uri, Object body) {
        return postForJson(CommonHelper.newURI(uri), body);
    }

    public JsonElement postForJson(URI uri, Object body) {
        return feignClientDefination.postJ(uri, gson.toJsonTree(body));
    }

    public JsonElement getForJson(String uri) {
        return getForJson(CommonHelper.newURI(uri));
    }

    public JsonElement getForJson(URI uri) {
        return feignClientDefination.getJ(uri);
    }

    public <T> T postForType(String uri, Map<String, String> headerMap, Object body, Class<T> type) {
        return gson.fromJson(postForJson(uri, headerMap, gson.toJsonTree(body)), type);
    }

    public <T> T postForType(URI uri, Map<String, String> headerMap, Object body, Class<T> type) {
        return gson.fromJson(postForJson(uri, headerMap, gson.toJsonTree(body)), type);
    }

    public <T> T postForType(String uri, Object body, Class<T> type) {
        return postForType(uri, Collections.emptyMap(), body, type);
    }

    public <T> T postForType(URI uri, Object body, Class<T> type) {
        return postForType(uri, Collections.emptyMap(), body, type);
    }

    public <T> T getForType(String uri, Map<String, String> headerMap, Class<T> type) {
        return gson.fromJson(getForJson(uri, headerMap), type);
    }

    public <T> T getForType(URI uri, Map<String, String> headerMap, Class<T> type) {
        return gson.fromJson(getForJson(uri, headerMap), type);
    }

    public <T> T getForType(String uri, Class<T> type) {
        return getForType(uri, Collections.emptyMap(), type);
    }

    public <T> T getForType(URI uri, Class<T> type) {
        return getForType(uri, Collections.emptyMap(), type);
    }

    public Response post(String uri, Map<String, String> headerMap, Object body) {
        return post(CommonHelper.newURI(uri), headerMap, gson.toJsonTree(body));
    }

    public Response post(URI uri, Map<String, String> headerMap, Object body) {
        return feignClientDefination.post(uri, headerMap, gson.toJsonTree(body));
    }

    public Response get(String uri, Map<String, String> headerMap) {
        return get(CommonHelper.newURI(uri), headerMap);
    }

    public Response get(URI uri, Map<String, String> headerMap) {
        return feignClientDefination.get(uri, headerMap);
    }

    public Response get(String uri) {
        return get(uri, Collections.emptyMap());
    }

    public Response get(URI uri) {
        return feignClientDefination.get(uri);
    }

    public Response post(String uri, Object body) {
        return post(CommonHelper.newURI(uri), body);
    }

    public Response post(URI uri, Object body) {
        return feignClientDefination.post(uri, gson.toJsonTree(body));
    }

    public Response request(UriWithMethod uriWithMethod, Map<String, String> headerMap, Object body) {
        if (uriWithMethod.getMethod() == HttpMethod.GET) {
            return get(uriWithMethod.getUri(), headerMap);
        } else if (uriWithMethod.getMethod() == HttpMethod.POST) {
            return post(uriWithMethod.getUri(), headerMap, body);
        }
        throw new ErrorException(ErrorCodeEnum.ILLEGAL_HTTP_METHOD);
    }

    public Response request(UriWithMethod uriWithMethod, Object body) {
        return request(uriWithMethod, Collections.EMPTY_MAP, body);
    }

    public Response request(UriWithMethod uriWithMethod) {
        return get(uriWithMethod.getUri());
    }

    public JsonElement requestForJson(UriWithMethod uriWithMethod, Map<String, String> headerMap, Object body) {
        if (uriWithMethod.getMethod() == HttpMethod.GET) {
            return getForJson(uriWithMethod.getUri(), headerMap);
        } else if (uriWithMethod.getMethod() == HttpMethod.POST) {
            return postForJson(uriWithMethod.getUri(), headerMap, body);
        }
        throw new ErrorException(ErrorCodeEnum.ILLEGAL_HTTP_METHOD);
    }

    public JsonElement requestForJson(UriWithMethod uriWithMethod, Object body) {
        return requestForJson(uriWithMethod, Collections.EMPTY_MAP, body);
    }

    public JsonElement requestForJson(UriWithMethod uriWithMethod) {
        return getForJson(uriWithMethod.getUri());
    }

    public String requestForText(UriWithMethod uriWithMethod, Map<String, String> headerMap, Object body) {
        if (uriWithMethod.getMethod() == HttpMethod.GET) {
            return getForText(uriWithMethod.getUri(), headerMap);
        } else if (uriWithMethod.getMethod() == HttpMethod.POST) {
            return postForText(uriWithMethod.getUri(), headerMap, body);
        }
        throw new ErrorException(ErrorCodeEnum.ILLEGAL_HTTP_METHOD);
    }

    public String requestForText(UriWithMethod uriWithMethod, Object body) {
        return requestForText(uriWithMethod, Collections.EMPTY_MAP, body);
    }

    public String requestForText(UriWithMethod uriWithMethod) {
        return getForText(uriWithMethod.getUri());
    }

}
