package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface AsyncRetryService extends OllgeiDisruptorService {
    /**
     * 启动.
     * @param context context
     * @return
     */
    void init(Object context);
}
