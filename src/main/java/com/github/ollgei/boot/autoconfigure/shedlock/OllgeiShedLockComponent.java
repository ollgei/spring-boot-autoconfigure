package com.github.ollgei.boot.autoconfigure.shedlock;

import java.util.concurrent.Callable;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.SimpleLock;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiShedLockComponent {
    /**全局锁*/
    String DEFAULT_NAME = "GLOBAL";

    /**
     * lock.
     */
    default void lock() {
        lock(DEFAULT_NAME);
    }

    /**
     * lock.
     * @param name name
     */
    void lock(String name);

    /**
     * lock.
     * @param lockConfiguration config
     * @return success
     */
    SimpleLock lock(LockConfiguration lockConfiguration);

    /**
     * unlock.
     * @param lock lock
     */
    void unlock(SimpleLock lock);

    /**
     * execute.
     * @param lockConfiguration lockConfiguration
     * @param runnable runnable
     */
    void execute(LockConfiguration lockConfiguration, Runnable runnable);

    /**
     * execute.
     * @param lockConfiguration lockConfiguration
     * @param callable callable
     * @return success
     */
    <T> T execute(LockConfiguration lockConfiguration, Callable<T> callable);

    /**
     * execute.
     * @param name name
     * @param runnable runnable
     */
    void execute(String name, Runnable runnable);

    /**
     * execute.
     * @param name name
     * @param callable callable
     * @return success
     */
    <T> T execute(String name, Callable<T> callable);

    /**
     * execute.
     * @param runnable runnable
     */
    default void execute(Runnable runnable) {
        execute(DEFAULT_NAME, runnable);
    }

    /**
     * execute.
     * @param callable callable
     * @return success
     */
    default <T> T execute(Callable<T> callable) {
        return execute(DEFAULT_NAME, callable);
    }

}
