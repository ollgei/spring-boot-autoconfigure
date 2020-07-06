package com.github.ollgei.spring.boot.autoconfigure.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * jdbcTemplate.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Data
@Builder
public class JdbcTemplateConfiguration {
    @NonNull
    private final JdbcTemplate jdbcTemplate;

    private final PlatformTransactionManager transactionManager;

    @NonNull
    private final String tableName;
}
