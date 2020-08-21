package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface AsyncRetryService {
    /**
     * 启动.
     * @param object object
     * @return
     */
    void start(AsyncRetryObject object);
    /**
     * 运行.
     * @param object object
     * @return
     */
    void run(AsyncRetryObject object);
}
