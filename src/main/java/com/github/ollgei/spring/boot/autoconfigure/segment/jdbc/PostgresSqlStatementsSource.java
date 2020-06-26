package com.github.ollgei.spring.boot.autoconfigure.segment.jdbc;

/**
 * PostgresSqlStatementsSource.
 *
 * @author jiawei
 * @since 1.0.0
 */
class PostgresSqlStatementsSource extends SqlStatementsSource {

    PostgresSqlStatementsSource(JdbcTemplateBoundSegmentConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String getInsertStatement() {
        return "";
//        return super.getInsertStatement() + " ON CONFLICT (" + name() + ") DO UPDATE " +
//                "SET " + lockUntil() + " = :lockUntil, " + lockedAt() + " = :now, " + lockedBy() + " = :lockedBy " +
//                "WHERE " + tableName() + "." + lockUntil() + " <= :now";
    }
}
