package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * event.
 *
 * @author jiawei
 * @since 1.0.0
 */
class InternalEvent {

   private AbstractDisruptorSubcription subcription;

    public AbstractDisruptorSubcription getSubcription() {
        return subcription;
    }

    public void setSubcription(AbstractDisruptorSubcription subcription) {
        this.subcription = subcription;
    }

    public int size() {
        return 0;
    }

    public void clear() {
        this.subcription = null;
    }

    public String getSimpleName() {
        return "event";
    }
}
