package com.github.ollgei.spring.boot.autoconfigure.shedlock;

import java.util.concurrent.Callable;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiShedLockComponent {
    String DEFAULT_NAME = "default";

    /**
     * desc.
     * @param name name
     * @param runnable runnable
     * @return
     */
    void execute(String name, Runnable runnable);

    /**
     * desc.
     * @param name name
     * @param callable callable
     * @return
     */
    <T> T execute(String name, Callable<T> callable);

    /**
     * desc.
     * @param runnable runnable
     * @return
     */
    default void execute(Runnable runnable) {
        execute(DEFAULT_NAME, runnable);
    }

    /**
     * desc.
     * @param callable callable
     * @return
     */
    default <T> T execute(Callable<T> callable) {
        return execute(DEFAULT_NAME, callable);
    }

}
