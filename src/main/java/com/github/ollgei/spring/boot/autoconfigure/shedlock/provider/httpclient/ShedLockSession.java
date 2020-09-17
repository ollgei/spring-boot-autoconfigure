package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

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
    private String lockAtMostFor;

    /**
     * Duration.
     */
    private String lockAtLeastFor;

}
