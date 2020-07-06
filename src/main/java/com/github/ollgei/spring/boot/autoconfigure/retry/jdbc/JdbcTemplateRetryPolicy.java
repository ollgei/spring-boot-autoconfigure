package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.context.RetryContextSupport;

import com.github.ollgei.spring.boot.autoconfigure.retry.RetryRepository;

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

    private RetryRepository retryRepository;

    public JdbcTemplateRetryPolicy(RetryRepository retryRepository) {
        this(DEFAULT_MAX_ATTEMPTS, retryRepository);
    }

    public JdbcTemplateRetryPolicy(int maxAttempts, RetryRepository retryRepository) {
        this.maxAttempts = maxAttempts;
        this.retryRepository = retryRepository;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        return context.getRetryCount() < maxAttempts;
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new SimpleJdbcTemplateRetryContext(parent);
    }

    @Override
    public void close(RetryContext context) {
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        retryRepository.registerThrowable(context);
    }

    private static class SimpleJdbcTemplateRetryContext extends RetryContextSupport {
        public SimpleJdbcTemplateRetryContext(RetryContext parent) {
            super(parent);
        }

        @Override
        public int getRetryCount() {
            final RetryContext parent = getParent();
            if (parent instanceof JdbcTemplateRetryContext) {
                return parent.getRetryCount();
            }
            return super.getRetryCount();
        }
    }
}
