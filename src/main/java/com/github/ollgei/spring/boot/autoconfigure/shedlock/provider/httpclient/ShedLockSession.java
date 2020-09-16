package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

import java.time.Duration;

import lombok.Data;

/**
 * object.
 * @author ollgei
 * @since 1.0
 */
@Data
public class ShedLockSession {
    /**
     * ID.
     */
    private String id;

    /**
     * name.
     */
    private String name;

    /**
     * Duration.
     */
    private Duration lockAtMostFor;

    /**
     * Duration.
     */
    private Duration lockAtLeastFor;

}
