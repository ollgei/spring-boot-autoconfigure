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

    public JdbcTemplateRetryContext(RetryContext parent) {
        super(parent);
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public int getRetryCount() {
        return cnt;
    }
}