package com.github.ollgei.boot.autoconfigure.httpclient;

/**
* Controls the level of logging.
 * @author ollgei
 * @since 1.0
*/
public enum FeignLoggerLevel {
    /**
     * No logging.
     */
    NONE,
    /**
     * Log only the request method and URL and the response status code and execution time.
     */
    BASIC,
    /**
     * Log the basic information along with request and response headers.
     */
    HEADERS,
    /**
     * Log the headers, body, and metadata for both requests and responses.
     */
    FULL;
}