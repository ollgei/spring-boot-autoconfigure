package com.github.ollgei.boot.autoconfigure.shedlock.provider.httpclient;

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
     */
    void unlock(String name);

}
