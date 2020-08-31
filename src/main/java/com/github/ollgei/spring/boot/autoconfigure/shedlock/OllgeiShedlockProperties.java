package com.github.ollgei.spring.boot.autoconfigure.shedlock;

import java.time.Duration;
import java.util.TimeZone;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = OllgeiShedlockProperties.PREFIX)
public class OllgeiShedlockProperties {
    public static final String PREFIX = "ollgei.shedlock";

    /**
     * lock most.
     */
    private Duration lockMostFor = Duration.parse("PT1M");
    /**
     * lock least.
     */
    private Duration lockLeastFor = Duration.parse("PT0S");
    /**
     * Jdbctemplate.
     */
    private Jdbctemplate jdbctemplate;

    @Data
    public static class Jdbctemplate {
        private String tableName;
        private TimeZone timeZone;
        private JdbcTemplateLockProvider.ColumnNames columns;
        private String lockedByValue;
        private boolean useDbTime;
    }

}
