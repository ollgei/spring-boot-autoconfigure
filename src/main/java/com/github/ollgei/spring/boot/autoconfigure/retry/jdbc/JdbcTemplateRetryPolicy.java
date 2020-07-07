package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;

import com.github.ollgei.spring.boot.autoconfigure.retry.RetryRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * policy.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateRetryPolicy extends SimpleRetryPolicy {

    private RetryRepository retryRepository;

    public JdbcTemplateRetryPolicy(RetryRepository retryRepository) {
        super();
        this.retryRepository = retryRepository;
    }

    public JdbcTemplateRetryPolicy(int maxAttempts, RetryRepository retryRepository) {
        super(maxAttempts);
        this.retryRepository = retryRepository;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        if (context instanceof SimpleJdbcTemplateRetryContext) {
            //执行过了
            if (context.getLastThrowable() != null) {
                return false;
            }
            return context.getRetryCount() < getMaxAttempts();
        }
        return super.canRetry(context);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        if (parent instanceof JdbcTemplateRetryContext) {
            boolean result = retryRepository.start((JdbcTemplateRetryContext) parent);
            if (result) {
                return new SimpleJdbcTemplateRetryContext((JdbcTemplateRetryContext) parent);
            }
            log.warn("database start error!!!");
        }
        return super.open(parent);
    }

    @Override
    public void close(RetryContext context) {
        if (context instanceof SimpleJdbcTemplateRetryContext) {
            retryRepository.close((JdbcTemplateRetryContext) context.getParent());
        }
        super.close(context);
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        if (context instanceof SimpleJdbcTemplateRetryContext) {
            retryRepository.registerThrowable((JdbcTemplateRetryContext) context.getParent());
        }
        super.registerThrowable(context, throwable);
    }

    private static class SimpleJdbcTemplateRetryContext extends RetryContextSupport {
        public SimpleJdbcTemplateRetryContext(JdbcTemplateRetryContext parent) {
            super(parent);
            final RuntimeException exception = new RuntimeException();
            for (int i = 0; i < parent.getRetryCount(); i++) {
                super.registerThrowable(exception);
            }
        }
    }
}
