package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import lombok.Data;
import lombok.ToString;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
@Data
@ToString(callSuper = true)
public class RetryableStringModel extends RetryableModel {

    /**
     * 参数
     */
    private String params;

    /**
     * 响应
     */
    private String response;

    /**
     * 返回参数
     */
    private String midstreamResponse;

    /**
     * 上游返回参数.
     */
    private String upstreamResponse;

    /**
     * 下游返回参数.
     */
    private String downstreamResponse;

}
