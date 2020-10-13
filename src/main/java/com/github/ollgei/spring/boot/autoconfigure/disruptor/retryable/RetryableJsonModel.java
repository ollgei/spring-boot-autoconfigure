package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import com.github.ollgei.base.commonj.gson.JsonElement;
import lombok.Data;
import lombok.ToString;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
@Data
@ToString(callSuper = true)
public class RetryableJsonModel extends RetryableModel {

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
    private JsonElement midstreamResponse;

    /**
     * 上游返回参数.
     */
    private JsonElement upstreamResponse;

    /**
     * 下游返回参数.
     */
    private JsonElement downstreamResponse;

}
