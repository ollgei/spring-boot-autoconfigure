package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import java.util.Collections;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.ollgei.spring.boot.autoconfigure.jdbc.AbstractJdbcTemplateRepository;
import com.github.ollgei.spring.boot.autoconfigure.retry.RetryProperties;
import com.github.ollgei.spring.boot.autoconfigure.retry.RetryRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * jdbc template repository.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateRetryRepository extends AbstractJdbcTemplateRepository implements RetryRepository {
    private final RetryProperties retryProperties;

    public JdbcTemplateRetryRepository(RetryProperties properties, @NonNull JdbcTemplate jdbcTemplate) {
        this(properties, jdbcTemplate, null);
    }

    public JdbcTemplateRetryRepository(RetryProperties properties, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        super(properties.getTableName(), jdbcTemplate, transactionManager);
        this.retryProperties = properties;
    }

    @Override
    public void registerThrowable(JdbcTemplateRetryContext context) {
        if (jdbcTemplate.update(registerThrowableSql(), Collections.singletonMap("names", context.getKeys())) <= 0) {
            log.warn("registerThrowable fail!!");
        }
    }

    @Override
    public boolean start(JdbcTemplateRetryContext context) {
        return jdbcTemplate.update(startSql(), Collections.singletonMap("names", context.getKeys())) == context.getKeys().size();
    }

    @Override
    public void close(JdbcTemplateRetryContext context) {
        if (jdbcTemplate.update(closeSql(), Collections.singletonMap("names", context.getKeys())) <= 0) {
            log.warn("close fail!!");
        }
    }

    private String registerThrowableSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET retry_count = retry_count + 1 WHERE IN (:names) and status = 1";
    }

    private String startSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET status = 1 WHERE name IN (:names) and status != 1";
    }

    private String closeSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET status = 2 WHERE name IN (:names)";
    }

}
