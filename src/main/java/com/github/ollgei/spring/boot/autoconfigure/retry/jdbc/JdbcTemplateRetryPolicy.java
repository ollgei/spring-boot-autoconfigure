package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.context.RetryContextSupport;

/**
 * policy.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryPolicy implements RetryPolicy {

    /**
     * The default limit to the number of attempts for a new policy.
     */
    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    private volatile int maxAttempts;

    public JdbcTemplateRetryPolicy() {
        this(DEFAULT_MAX_ATTEMPTS);
    }

    public JdbcTemplateRetryPolicy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        return context.getRetryCount() < maxAttempts;
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new JdbcTemplateRetryContext(parent);
    }

    @Override
    public void close(RetryContext context) {
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        //保存数据库
    }

    private static class JdbcTemplateRetryContext extends RetryContextSupport {
        public JdbcTemplateRetryContext(RetryContext parent) {
            super(parent);
        }
    }
}
