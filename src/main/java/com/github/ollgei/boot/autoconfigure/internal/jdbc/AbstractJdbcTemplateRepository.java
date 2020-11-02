package com.github.ollgei.boot.autoconfigure.internal.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.NonNull;

/**
 * jdbc template repository.
 * @author ollgei
 * @since 1.0.0
 */
public abstract class AbstractJdbcTemplateRepository {
    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final TransactionTemplate transactionTemplate;
    protected final SqlStatementsSource sqlStatementsSource;

    protected AbstractJdbcTemplateRepository(String tableName, @NonNull JdbcTemplate jdbcTemplate) {
        this(tableName, jdbcTemplate, null);
    }

    protected AbstractJdbcTemplateRepository(String tableName, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this(tableName, JdbcTemplateConfiguration.builder()
                .jdbcTemplate(jdbcTemplate)
                .transactionManager(transactionManager)
                .tableName(tableName)
                .build()
        );
    }

    private AbstractJdbcTemplateRepository(String tableName, @NonNull JdbcTemplateConfiguration configuration) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(configuration.getJdbcTemplate());
        this.sqlStatementsSource = SqlStatementsSource.create(configuration);
        this.transactionTemplate = createTransactionTemplate(configuration);
    }

    private TransactionTemplate createTransactionTemplate(JdbcTemplateConfiguration configuration) {
        final PlatformTransactionManager transactionManager = configuration.getTransactionManager() != null ?
                configuration.getTransactionManager() :
                new DataSourceTransactionManager(configuration.getJdbcTemplate().getDataSource());
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return transactionTemplate;
    }
}
