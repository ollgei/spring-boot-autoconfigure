package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

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
public class DisruptorWorkerHandler implements WorkHandler<Event> {

    private final Executor executor;

    private final DisruptorSubscriber subscriber;

    /**
     * Instantiates a new Route data handler.
     *
     * @param executor        the executor
     */
    public DisruptorWorkerHandler(final Executor executor, final DisruptorSubscriber subscriber) {
        this.executor = executor;
        this.subscriber = subscriber;
    }

    @Override
    public void onEvent(final Event event) {
        final DisruptorSubcription subcription = event.getSubcription();
        if (log.isInfoEnabled()) {
            final int size = event.size();
            log.info("subscriber->Thread:{}, sequence:{}, size: {}",
                    Thread.currentThread().getName(),
                    subcription.getSequence(),
                    size);
        }
        event.clear();
        executor.execute(() -> subscriber.onNext(subcription));
    }
}
