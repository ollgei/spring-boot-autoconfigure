package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiDisruptorService<C extends OllgeiDisruptorContext> {
    /**
     * 类型.
     * @return
     */
    String kind();
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
}
