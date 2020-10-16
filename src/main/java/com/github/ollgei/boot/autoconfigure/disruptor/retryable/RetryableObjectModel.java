package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.Data;
import lombok.ToString;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
@Data
@ToString(callSuper = true)
public class RetryableObjectModel extends RetryableModel {

    /**
     * 参数
     */
    private Object params;

    /**
     * 响应
     */
    private Object response;

    /**
     * 返回参数
     */
    private Object midstreamResponse;

    /**
     * 上游返回参数.
     */
    private Object upstreamResponse;

    /**
     * 下游返回参数.
     */
    private Object downstreamResponse;

}
