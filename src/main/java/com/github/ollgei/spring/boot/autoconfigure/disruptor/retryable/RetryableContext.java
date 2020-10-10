package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

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
    /**BIZ ID:subID*/
    private String bizId;
    /**BIZ seq NO*/
    private Integer bizSeqNo;
    /**upstream channel*/
    private String channel;
    /**data*/
    private Object data;
    /**data*/
    private Object responseData;

    public <T> T resolveData() {
       return (T) data;
    }

    public <T> T resolveResponseData() {
        return (T) responseData;
    }

}
