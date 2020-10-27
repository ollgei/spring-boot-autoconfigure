package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
public class RetryableTopic<T> {

    private T request;

    private T response;

}
