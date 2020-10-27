package com.github.ollgei.boot.autoconfigure.disruptor.core;

import java.util.concurrent.CountDownLatch;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiDisruptorService<C> {
    /**
     * 初始化.
     * @param context context
     */
    default void writeAndPublish(C context) {
        writeAndPublish(context, null);
    }

    /**
     * 初始化.
     * @param context context
     */
    default void writeAndPublish(C context, CountDownLatch countDownLatch) {
        write(context);
        publish(context, countDownLatch);
    }

    /**
     * 初始化.
     * @param context context
     */
    void publish(C context, CountDownLatch countDownLatch);
    /**
     * 持久化到数据库中.
     * @param context context
     */
    void write(C context);
    /**
     * 执行.
     * @param context context
     */
    void read(C context, CountDownLatch countDownLatch);
    /**
     * 执行.
     * @param context context
     */
    default void read(C context) {
        read(context, null);
    }

    /**
     * 清理.
     * @param context context
     */
    default void cleanup(C context) {

    }

    /**
     * 安全执行.
     * @param context context
     * @param countDownLatch latch
     */
    default void safeRead(C context, CountDownLatch countDownLatch) {
        if (!lock(context)) {
            return;
        }
        try {
            read(context, countDownLatch);
        } finally {
            unlock(context);
        }
    }

    /**
     * lock.
     * @param context context
     * @return true:next false:stop
     */
    default boolean lock(C context) {
        return true;
    }
    /**
     * unlock.
     * @param context context
     */
    default void unlock(C context) {

    }
}
