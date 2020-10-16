package com.github.ollgei.boot.autoconfigure.shedlock;

import java.time.Duration;
import java.util.TimeZone;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

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
    private Jdbctemplate jdbctemplate = new Jdbctemplate();
    /**
     * Httpclient.
     */
    private Httpclient httpclient = new Httpclient();

    @Data
    public static class Jdbctemplate {
        private String tableName;
        private TimeZone timeZone;
        private String columnName = "name";
        private String columnLockUntil = "lock_until";
        private String columnLockedAt = "locked_at";
        private String columnLockedBy = "locked_by";
        private String lockedByValue;
        private boolean useDbTime;
    }

    @Data
    public static class Httpclient {
        private Duration minSessionTtl = Duration.ofSeconds(10);
        private Duration gracefulShutdownInterval = Duration.ofSeconds(2);
    }
}
