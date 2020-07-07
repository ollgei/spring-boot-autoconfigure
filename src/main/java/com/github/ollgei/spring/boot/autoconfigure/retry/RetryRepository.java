package com.github.ollgei.spring.boot.autoconfigure.retry;

import com.github.ollgei.spring.boot.autoconfigure.retry.jdbc.JdbcTemplateRetryContext;

/**
 * repository.
 * @author ollgei
 * @since 1.0.0
 */
public interface RetryRepository {

    /**
     * save error.
     * @param context context
     * @return
     */
    void registerThrowable(JdbcTemplateRetryContext context);

    /**
     * start.
     * @param context context
     * @return true success false fail
     */
    boolean start(JdbcTemplateRetryContext context);

    /**
     * start.
     * @param context context
     * @return true success false fail
     */
    void close(JdbcTemplateRetryContext context);

}
