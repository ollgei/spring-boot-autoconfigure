package com.github.ollgei.spring.boot.autoconfigure.retry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.RetryCacheCapacityExceededException;
import org.springframework.retry.policy.RetryContextCache;

/**
 * cache.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryContextCache implements RetryContextCache {

    @Override
    public RetryContext get(Object key) {
        return null;
    }

    @Override
    public void put(Object key, RetryContext context) throws RetryCacheCapacityExceededException {

    }

    @Override
    public void remove(Object key) {

    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    public static void main(String[] args) {
        Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
        map.put("aa", "123");
        map.put("aa", "456");

        System.out.println(map.get("aa"));

    }
}
