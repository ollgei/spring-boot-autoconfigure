package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import java.util.List;

/**
 * OllgeiDisruptorSimpleSubscriber.
 * @author ollgei
 * @since 1.0.0
 */
public class OllgeiDisruptorSimpleSubscriber implements OllgeiDisruptorSubscriber<OllgeiDisruptorSimpleSubscription> {

    private List<OllgeiDisruptorService> disruptorServiceList;

    public OllgeiDisruptorSimpleSubscriber(List<OllgeiDisruptorService> disruptorServiceList) {
        this.disruptorServiceList = disruptorServiceList;
    }

    @Override
    public void onNext(OllgeiDisruptorSimpleSubscription subscription) {
        disruptorServiceList.stream().
                filter(s -> s.kind() != null && s.kind().equals(subscription.getKind())).
                forEach(s -> s.run(subscription.getContext()));
    }
}
