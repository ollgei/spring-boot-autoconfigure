package com.github.ollgei.spring.boot.autoconfigure.segment.jdbc;

import org.springframework.jdbc.core.ConnectionCallback;

import lombok.extern.slf4j.Slf4j;

/**
 * CREATE TABLE `uuid_alloc` (
 * `biz_tag` varchar(128)  NOT NULL DEFAULT '', -- your biz unique name
 * `max_id` bigint(20) NOT NULL DEFAULT '1',
 * `step` int(11) NOT NULL,
 * `description` varchar(256)  DEFAULT NULL,
 * `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * PRIMARY KEY (`biz_tag`)
 * ) ENGINE=InnoDB.
 * @author jiawei
 * @since 1.0.0
 */
@Slf4j
public class SqlStatementsSource {
    private final JdbcTemplateBoundSegmentConfiguration configuration;

    public SqlStatementsSource(JdbcTemplateBoundSegmentConfiguration configuration) {
        this.configuration = configuration;
    }

    public static SqlStatementsSource create(JdbcTemplateBoundSegmentConfiguration configuration) {
        String databaseProductName = getDatabaseProductName(configuration);
        if ("PostgreSQL".equals(databaseProductName)) {
            log.debug("Using PostgresSqlStatementsSource");
            return new PostgresSqlStatementsSource(configuration);
        } else {
            log.debug("Using SqlStatementsSource");
            return new SqlStatementsSource(configuration);
        }
    }

    private static String getDatabaseProductName(JdbcTemplateBoundSegmentConfiguration configuration) {
        return configuration.getJdbcTemplate().execute((ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
    }

    public String getInsertStatement() {
        return "INSERT INTO " + tableName() + "(person_id, name) VALUES(:personId, :name)";
    }

    /**
     * SELECT
     * 		ID,
     * 		HOST_NAME,
     * 		PORT,
     * 		TYPE,
     * 		LAUNCH_DATE,
     * 		MODIFIED,
     * 		CREATED
     * 		FROM
     * 		WORKER_NODE
     * 		WHERE
     * 		HOST_NAME = #{host} AND PORT = #{port}.
     */
    public String getInsertWorkNodeStatement() {
        return "INSERT INTO " + tableName() + "(HOST_NAME, PORT, TYPE, LAUNCH_DATE, MODIFIED, CREATED) VALUES(:hostName, :port, :type, :launchDate, NOW(), NOW())";
    }

    public String listAllocators() {
        return "SELECT name, max_id, step, update_time FROM " + tableName();
    }

    public String getAllocator() {
        return "SELECT name, max_id, step, update_time FROM " + tableName() + " WHERE name = :name";
    }

    public String listTags() {
        return "SELECT name FROM " + tableName();
    }

    public String tableName() {
        return configuration.getTableName();
    }

    public String getUpdateMaxId() {
        return "UPDATE " + tableName() + " SET max_id = max_id + step WHERE name = :name";
    }

    public String getUpdateMaxIdByCustomStep() {
        return "UPDATE " + tableName() + " SET max_id = max_id + :step WHERE name = :name";
    }
}
