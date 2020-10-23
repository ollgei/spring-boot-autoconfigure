package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * context.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public abstract class RetryableContext {

    /**APP ID*/
    private String appId;
    /**kind*/
    private String kind;
    /**tag*/
    private String tag;
    /**seqNo*/
    private Integer seqNo;
    /**upstream channel*/
    private String channel;
    /**request IP*/
    private String requestIp;

    public RetryableContext() {
        this.seqNo = 0;
    }
    /**
     * desc.
     * @return params
     */
    public abstract Object resolveParams();
    /**
     * desc.
     * @return params type
     */
    public abstract Class<?> resolveParamsType();
    /**
     * desc.
     * @return response
     */
    public abstract Object resolveResponse();
    /**
     * desc.
     * @return response type
     */
    public abstract Class<?> resolveResponseType();

}
