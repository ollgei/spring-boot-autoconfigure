package com.github.ollgei.spring.boot.autoconfigure.retry;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = RetryProperties.PREFIX)
public class RetryProperties {

    public static final String PREFIX = "ollgei.retry";

    private String tableName = "t_retry";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
