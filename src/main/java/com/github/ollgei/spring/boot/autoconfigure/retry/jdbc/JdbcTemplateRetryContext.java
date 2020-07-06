package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryContext extends RetryContextSupport {
    private int cnt;
    private String key;

    public JdbcTemplateRetryContext(String key, RetryContext parent) {
        super(parent);
        this.key = key;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getRetryCount() {
        return cnt;
    }
}