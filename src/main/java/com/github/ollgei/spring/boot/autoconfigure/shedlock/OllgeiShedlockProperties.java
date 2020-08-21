package com.github.ollgei.spring.boot.autoconfigure.shedlock;

import java.time.Duration;

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

}
