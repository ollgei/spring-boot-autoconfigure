package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import lombok.ToString;

/**
 * subscription.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ToString(callSuper = true)
public class OllgeiDisruptorSimpleSubscription extends AbstractSubscription {
    /**类型*/
    private String kind;
    /**上下文数据*/
    private Object context;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }
}
