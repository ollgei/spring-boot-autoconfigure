package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
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
    @Builder.Default
    private Integer bizSeqNo = 0;
    /**upstream channel*/
    private String channel;
    /**request IP*/
    private String requestIp;
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