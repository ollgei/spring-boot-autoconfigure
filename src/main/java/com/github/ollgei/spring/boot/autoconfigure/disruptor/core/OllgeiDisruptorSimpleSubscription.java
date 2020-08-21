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
    private OllgeiDisruptorContext context;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public OllgeiDisruptorContext getContext() {
        return context;
    }

    public void setContext(OllgeiDisruptorContext context) {
        this.context = context;
    }
}
