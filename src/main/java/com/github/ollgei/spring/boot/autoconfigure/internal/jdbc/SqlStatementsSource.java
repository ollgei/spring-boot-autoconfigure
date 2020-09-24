package com.github.ollgei.spring.boot.autoconfigure.internal.jdbc;

import org.springframework.jdbc.core.ConnectionCallback;

/**
 * SqlStatementsSource.
 *
 *  if PostgreSQL insert:
 *  super.getInsertStatement() + " ON CONFLICT (" + name() + ") DO UPDATE " +
 *    "SET " + lockUntil() + " = :lockUntil, " + lockedAt() + " = :now, " + lockedBy() + " = :lockedBy " +
 *    "WHERE " + tableName() + "." + lockUntil() + " @lt;= :now";
 * @author ollgei
 * @since 1.0.0
 */
public class SqlStatementsSource {
    private final JdbcTemplateConfiguration configuration;

    public SqlStatementsSource(JdbcTemplateConfiguration configuration) {
        this.configuration = configuration;
    }

    public static SqlStatementsSource create(JdbcTemplateConfiguration configuration) {
        return new SqlStatementsSource(configuration);
    }

    private static String getDatabaseProductName(JdbcTemplateConfiguration configuration) {
        return configuration.getJdbcTemplate().execute((ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
    }

    public String tableName() {
        return configuration.getTableName();
    }

    public boolean ifPostgreSQL() {
        String databaseProductName = getDatabaseProductName(configuration);
        return "PostgreSQL".equals(databaseProductName);
    }
}
