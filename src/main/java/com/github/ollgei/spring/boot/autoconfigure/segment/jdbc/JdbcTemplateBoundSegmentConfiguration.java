package com.github.ollgei.spring.boot.autoconfigure.segment.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * jdbcTemplate.
 *
 * @author jiawei
 * @since 1.0.0
 */
@Data
@Builder
class JdbcTemplateBoundSegmentConfiguration {
    @NonNull
    private final JdbcTemplate jdbcTemplate;

    private final PlatformTransactionManager transactionManager;

    @NonNull
    private final String tableName;
}
