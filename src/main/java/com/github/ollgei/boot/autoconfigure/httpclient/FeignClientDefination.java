package com.github.ollgei.boot.autoconfigure.httpclient;

import java.net.URI;
import java.util.Map;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.utils.CommonHelper;
import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;
import feign.Response;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

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

    @Slf4j
    final class Fallback implements FeignClientDefination {

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
    final class FactoryFallback implements FallbackFactory<FeignClientDefination> {

        private FeignClientDefination fallback;

        public FactoryFallback() {
            this(new Fallback());
        }

        public FactoryFallback(FeignClientDefination defination) {
            this.fallback = defination == null ? new Fallback() : defination;
        }

        @Override
        public FeignClientDefination create(Throwable cause) {
            log.warn("失败原因:{}", CommonHelper.throwableToString(cause));
            return this.fallback;
        }
    }
}
