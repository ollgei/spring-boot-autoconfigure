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
public class RetryableBytesModel extends RetryableModel {

    /**
     * 参数
     */
    private byte[] params;

    /**
     * 响应
     */
    private byte[] response;

    /**
     * 返回参数
     */
    private byte[] midstreamResponse;

    /**
     * 上游返回参数.
     */
    private byte[] upstreamResponse;

    /**
     * 下游返回参数.
     */
    private byte[] downstreamResponse;

}
