package com.github.ollgei.boot.autoconfigure.segment.core;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 重新加载Tag事件.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class NumberElementReloadEventListener implements SmartApplicationListener {

    private AtomicBoolean ready = new AtomicBoolean(false);

    private NumberBoundSegmentBuffer buffer;

    public NumberElementReloadEventListener(NumberBoundSegmentBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType)
                || NumberElementReloadEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            handle((ApplicationReadyEvent) event);
        } else if (event instanceof NumberElementReloadEvent) {
            handle((NumberElementReloadEvent) event);
        }
    }

    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    public void handle(NumberElementReloadEvent event) {
        // don't handle events before app is ready
        if (this.ready.get()) {
            log.info("Event received " + event.getEventDesc());
            buffer.safeReload(false);
            log.info("Reload tag");
        }
    }

}
