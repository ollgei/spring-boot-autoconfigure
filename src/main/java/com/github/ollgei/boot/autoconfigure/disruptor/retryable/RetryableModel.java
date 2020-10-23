package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
@Data
public class RetryableModel {

    /**
     * 业务类型
     */
    private String kind;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * TAG
     */
    private String tag;

    /**
     * 序列号.
     */
    private Short seqNo;

    /**
     * 状态: 0 未开始 第1位 本地调用 第2位 上游调用 第三位 下游调用
     */
    private Integer state;

    private Integer retryCount;

    private Long nextRetryTimestamp = System.currentTimeMillis();

    private Integer retryIncrCount;

    private Long nextRetryIncrTimestamp;

}
