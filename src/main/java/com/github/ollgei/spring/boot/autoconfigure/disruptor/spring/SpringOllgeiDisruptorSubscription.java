package com.github.ollgei.spring.boot.autoconfigure.disruptor.spring;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.AbstractSubscription;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
import lombok.ToString;

/**
 * subscription.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ToString(callSuper = true)
public class SpringOllgeiDisruptorSubscription extends AbstractSubscription {
    /**类型*/
    private Class<? extends OllgeiDisruptorService> clazz;
    /**上下文数据*/
    private OllgeiDisruptorContext context;

    public Class<? extends OllgeiDisruptorService> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends OllgeiDisruptorService> clazz) {
        this.clazz = clazz;
    }

    public OllgeiDisruptorContext getContext() {
        return context;
    }

    public void setContext(OllgeiDisruptorContext context) {
        this.context = context;
    }
}
