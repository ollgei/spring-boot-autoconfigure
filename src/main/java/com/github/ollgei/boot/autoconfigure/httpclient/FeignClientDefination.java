package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.URI;
import java.util.Map;

import com.github.ollgei.base.commonj.gson.JsonElement;
import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;
import feign.Response;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public interface FeignClientDefination {

    /**
     * post.
     * @param uri uri
     * @param headerMap map
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    Response post(URI uri, @HeaderMap Map<String, String> headerMap, JsonElement body);

    /**
     * post.
     * @param uri uri
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    Response post(URI uri, JsonElement body);

    /**
     * desc.
     * @param uri uri
     * @param headerMap map
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    Response get(URI uri, @HeaderMap Map<String, String> headerMap);

    /**
     * desc.
     * @param uri uri
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    Response get(URI uri);


    /**
     * post.
     * @param uri uri
     * @param headerMap map
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    JsonElement postJ(URI uri, @HeaderMap Map<String, String> headerMap, JsonElement body);

    /**
     * post.
     * @param uri uri
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    JsonElement postJ(URI uri, JsonElement body);

    /**
     * desc.
     * @param uri uri
     * @param headerMap map
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    JsonElement getJ(URI uri, @HeaderMap Map<String, String> headerMap);

    /**
     * desc.
     * @param uri uri
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    JsonElement getJ(URI uri);

    /**
     * post.
     * @param uri uri
     * @param headerMap map
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    String postS(URI uri, @HeaderMap Map<String, String> headerMap, JsonElement body);

    /**
     * post.
     * @param uri uri
     * @param body  body
     * @return
     */
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    String postS(URI uri, JsonElement body);

    /**
     * desc.
     * @param uri uri
     * @param headerMap map
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    String getS(URI uri, @HeaderMap Map<String, String> headerMap);

    /**
     * desc.
     * @param uri uri
     * @return
     */
    @RequestLine("GET")
    @Headers({"Content-Type: application/json"})
    String getS(URI uri);
}
