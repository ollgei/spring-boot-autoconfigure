package com.github.ollgei.boot.autoconfigure.disruptor.core;

/**
 * event.
 *
 * @author jiawei
 * @since 1.0.0
 */
class InternalEvent {

   private AbstractSubscription subscription;

    public AbstractSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(AbstractSubscription subscription) {
        this.subscription = subscription;
    }

    public int size() {
        return 0;
    }

    public void clear() {
        this.subscription = null;
    }

    public String getSimpleName() {
        return "event";
    }
}
