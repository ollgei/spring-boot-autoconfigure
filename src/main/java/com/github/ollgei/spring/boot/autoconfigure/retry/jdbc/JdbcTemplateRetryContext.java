package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import java.util.List;

import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryContext extends RetryContextSupport {
    private int cnt;
    private List<String> keys;

    public JdbcTemplateRetryContext(RetryContext parent, List<String> keys) {
        super(parent);
        this.keys = keys;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<String> getKeys() {
        return keys;
    }

    @Override
    public int getRetryCount() {
        return cnt;
    }
}