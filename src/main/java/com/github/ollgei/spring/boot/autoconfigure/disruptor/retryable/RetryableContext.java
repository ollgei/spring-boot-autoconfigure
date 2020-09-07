package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
import lombok.Getter;
import lombok.Setter;

/**
 * context.
 * @author ollgei
 * @since 1.0.0
 */
@Getter
@Setter
public class RetryableContext extends OllgeiDisruptorContext {

    /**APP ID*/
    private String appId;
    /**kind*/
    private String bizKind;
    /**BIZ ID*/
    private String bizId;
    /**BI Sub No*/
    private Integer bizSubNo;

}
