package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.concurrent.CountDownLatch;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
public class RetryableTopic<T> {

    private String serviceName;

    private T request;

    private T response;

    private CountDownLatch latch;

}
