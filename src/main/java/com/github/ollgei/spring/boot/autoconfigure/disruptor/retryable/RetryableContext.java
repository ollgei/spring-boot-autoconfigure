package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import java.util.concurrent.CountDownLatch;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
import lombok.Builder;
import lombok.Data;

/**
 * context.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@Builder
public class RetryableContext extends OllgeiDisruptorContext {

    /**APP ID*/
    private String appId;
    /**kind*/
    private String bizKind;
    /**BIZ ID*/
    private String bizId;
    /**BI Sub No*/
    private Integer bizSubNo;
    /**data*/
    private Object data;
    /**data*/
    private Object responseData;
    /**latch*/
    private CountDownLatch latch;

}
