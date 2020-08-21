package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface OllgeiDisruptorService {
    /**
     * 类型.
     * @return
     */
    String kind();
    /**
     * 执行.
     * @param context context
     * @return
     */
    void run(Object context);
}
