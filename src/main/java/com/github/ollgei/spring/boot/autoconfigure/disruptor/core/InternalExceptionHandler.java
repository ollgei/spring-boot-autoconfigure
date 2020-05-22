package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

/**
 * exception handler.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
class InternalExceptionHandler implements ExceptionHandler {

    private Disruptor<InternalEvent> disruptor;

    public InternalExceptionHandler(Disruptor disruptor) {
        this.disruptor = disruptor;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        ex.printStackTrace();
        log.error("Exception occurred while processing a {}.",
                ((InternalEvent) event).getSimpleName(), ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        log.error("Failed to start the DisruptorCommandBus.", ex);
        disruptor.shutdown();
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        log.error("Error while shutting down the DisruptorCommandBus", ex);
    }
}
