package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import lombok.Data;

/**
 * retry object.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class AsyncRetryObject {

    /***version == 1***/
    /**版本号*/
    private Integer version = 1;
    /**类型*/
    private String kind;
    /**请求上下文*/
    private Object context;

}
