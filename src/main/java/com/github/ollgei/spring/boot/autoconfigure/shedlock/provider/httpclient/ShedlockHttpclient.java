package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

/**
 * http client.
 * @author ollgei
 * @since 1.0
 */
public interface ShedlockHttpclient {

    /**
     * desc.
     * @param session object
     * @return true/false
     */
    boolean tryLock(ShedLockSession session);

    /**
     * desc.
     * @param name name
     * @return
     */
    void unlock(String name);

}
