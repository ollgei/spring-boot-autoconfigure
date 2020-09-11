package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiDisruptorService<C extends OllgeiDisruptorContext> {
    /**
     * 初始化.
     * @param context context
     * @return
     */
    default void writeAndPublish(C context) {
        write(context);
        publish(context);
    }

    /**
     * 初始化.
     * @param context context
     * @return
     */
    void publish(C context);
    /**
     * 持久化到数据库中.
     * @param context context
     * @return
     */
    void write(C context);
    /**
     * 执行.
     * @param context context
     * @return
     */
    void read(C context);

    /**
     * 清理.
     * @param context context
     * @return
     */
    default void cleanup(C context) {

    }

    /**
     * 安全执行.
     * @param context context
     * @return
     */
    default void safeRead(C context) {
        if (!lock(context)) {
            return;
        }
        try {
            read(context);
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
     * @return
     */
    default void unlock(C context) {

    }
}
