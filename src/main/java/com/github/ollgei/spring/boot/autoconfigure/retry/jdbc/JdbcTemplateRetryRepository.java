package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.RetryContext;
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
    public void registerThrowable(RetryContext context) {
        jdbcTemplate.update("", new HashMap<>());
    }

}
