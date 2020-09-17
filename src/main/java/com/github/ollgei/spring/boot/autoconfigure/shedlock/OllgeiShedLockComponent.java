package com.github.ollgei.spring.boot.autoconfigure.shedlock;

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
     * desc.
     * @param lockConfiguration config
     * @return
     */
    SimpleLock lock(LockConfiguration lockConfiguration);

    /**
     * desc.
     * @param lock lock
     * @return
     */
    void unlock(SimpleLock lock, LockConfiguration lockConfiguration);

    /**
     * execute.
     * @param lockConfiguration lockConfiguration
     * @param runnable runnable
     * @return
     */
    void execute(LockConfiguration lockConfiguration, Runnable runnable);

    /**
     * execute.
     * @param lockConfiguration lockConfiguration
     * @param callable callable
     * @return
     */
    <T> T execute(LockConfiguration lockConfiguration, Callable<T> callable);

    /**
     * execute.
     * @param name name
     * @param runnable runnable
     * @return
     */
    void execute(String name, Runnable runnable);

    /**
     * execute.
     * @param name name
     * @param callable callable
     * @return
     */
    <T> T execute(String name, Callable<T> callable);

    /**
     * execute.
     * @param runnable runnable
     * @return
     */
    default void execute(Runnable runnable) {
        execute(DEFAULT_NAME, runnable);
    }

    /**
     * execute.
     * @param callable callable
     * @return
     */
    default <T> T execute(Callable<T> callable) {
        return execute(DEFAULT_NAME, callable);
    }

}
