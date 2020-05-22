package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * event.
 *
 * @author jiawei
 * @since 1.0.0
 */
class InternalEvent {

   private AbstractSubcription subcription;

    public AbstractSubcription getSubcription() {
        return subcription;
    }

    public void setSubcription(AbstractSubcription subcription) {
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
