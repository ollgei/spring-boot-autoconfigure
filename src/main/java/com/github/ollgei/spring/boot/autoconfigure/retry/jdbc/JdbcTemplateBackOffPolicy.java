package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

import com.github.ollgei.spring.boot.autoconfigure.retry.RetryRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateBackOffPolicy implements BackOffPolicy {

    private RetryRepository retryRepository;

    public JdbcTemplateBackOffPolicy(RetryRepository retryRepository) {
        this.retryRepository = retryRepository;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return null;
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        //设定下一次触发的时间
    }
}
