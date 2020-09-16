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
     * @return lock id
     */
    String tryLock(ShedLockSession object);

    /**
     * desc.
     * @param lockId lockId
     * @return
     */
    void unlock(String lockId);

}
