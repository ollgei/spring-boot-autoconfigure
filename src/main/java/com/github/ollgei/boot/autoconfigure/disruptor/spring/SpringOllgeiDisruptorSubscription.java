package com.github.ollgei.boot.autoconfigure.disruptor.spring;

import java.util.concurrent.CountDownLatch;

import com.github.ollgei.boot.autoconfigure.disruptor.core.AbstractSubscription;
import com.github.ollgei.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
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
    private Object context;
    /**CountDownLatch*/
    private CountDownLatch countDownLatch;

    public Class<? extends OllgeiDisruptorService> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends OllgeiDisruptorService> clazz) {
        this.clazz = clazz;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
