package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.AbstractSubscription;
import lombok.Data;

@Data
public class AsyncRetrySubscription extends AbstractSubscription {
    /**类型*/
    private String kind;
    /**上下文数据*/
    private Object context;
}
