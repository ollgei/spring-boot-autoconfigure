package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

/**
 * http client.
 * @author ollgei
 * @since 1.0
 */
public interface ShedlockHttpclient {

    /**
     * desc.
     * @param object object
     * @return true/false
     */
    boolean tryLock(ShedLockSession object);

    /**
     * desc.
     * @param name name
     * @return
     */
    void unlock(String name);

}
