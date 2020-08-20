package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import lombok.Data;

/**
 * retry object.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryObject {

    public static final int MAX_VERSION = 1;
    /***version == 1***/
    /**版本号*/
    private Integer version = MAX_VERSION;
    /**请求数据*/
    private Object request;
    /**上下文数据*/
    private Object context;

}
