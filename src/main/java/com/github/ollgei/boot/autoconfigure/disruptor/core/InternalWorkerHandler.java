package com.github.ollgei.boot.autoconfigure.disruptor.core;

import java.util.concurrent.Executor;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * </p>
 *
 * @author jiawei
 * @since 1.0.0
 */
@Slf4j
class InternalWorkerHandler implements WorkHandler<InternalEvent> {

    private final Executor executor;

    private final OllgeiDisruptorSubscriber subscriber;

    /**
     * Instantiates a new Route data handler.
     *
     * @param executor        the executor
     */
    public InternalWorkerHandler(final Executor executor, final OllgeiDisruptorSubscriber subscriber) {
        this.executor = executor;
        this.subscriber = subscriber;
    }

    @Override
    public void onEvent(final InternalEvent event) {
        try {
            final AbstractSubscription subscription = event.getSubscription();
            if (log.isInfoEnabled()) {
                final int size = event.size();
                log.info("subscriber->Thread:{}, sequence:{}, size: {}",
                        Thread.currentThread().getName(),
                        subscription.getSequence(),
                        size);
            }
            executor.execute(() -> subscriber.onNext(subscription));
        } finally {
            event.clear();
        }
    }
}
