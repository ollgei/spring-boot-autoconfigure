package com.github.ollgei.spring.boot.autoconfigure.retry;

import java.util.List;

import com.github.ollgei.spring.boot.autoconfigure.retry.jdbc.JdbcTemplateRetryContext;
import com.github.ollgei.spring.boot.autoconfigure.retry.jdbc.RetryRecord;

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

    /**
     * desc.
     * @param context context
     * @return 
     */
    List<RetryRecord> listRecord(JdbcTemplateRetryContext context);

}
