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
    void init(C context);
    /**
     * 执行.
     * @param context context
     * @return
     */
    void run(C context);
}
