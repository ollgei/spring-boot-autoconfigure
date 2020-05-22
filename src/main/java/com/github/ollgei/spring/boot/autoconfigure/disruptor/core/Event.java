package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * event.
 *
 * @author jiawei
 * @since 1.0.0
 */
class Event {

   private DisruptorSubcription subcription;

    public DisruptorSubcription getSubcription() {
        return subcription;
    }

    public void setSubcription(DisruptorSubcription subcription) {
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
