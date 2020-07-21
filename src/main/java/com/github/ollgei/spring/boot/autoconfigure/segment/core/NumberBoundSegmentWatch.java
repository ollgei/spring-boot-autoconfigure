package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.StringJoiner;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * watch.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class NumberBoundSegmentWatch implements ApplicationEventPublisherAware, SmartLifecycle {

    private final BoundSegmentProperties properties;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final TaskScheduler taskScheduler;

    private ApplicationEventPublisher publisher;

    private final BoundSegmentRepository repository;

    private ScheduledFuture<?> watchFuture;

    private boolean firstTime = true;

    public NumberBoundSegmentWatch(BoundSegmentProperties properties, BoundSegmentRepository repository) {
        this(properties, repository, getTaskScheduler());
    }

    public NumberBoundSegmentWatch(BoundSegmentProperties properties, BoundSegmentRepository repository, TaskScheduler taskScheduler) {
        this.properties = properties;
        this.repository = repository;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            this.watchFuture = this.taskScheduler.scheduleWithFixedDelay(
                    this::watchSegment, this.properties.getWatch().getDelay());
        }
    }

    private void watchSegment() {
        if (this.running.get()) {
            NumberElementReloadEventData data = new NumberElementReloadEventData();
            this.publisher.publishEvent(
                    new NumberElementReloadEvent(this, data, data.toString()));
        }
        this.firstTime = false;
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false) && this.watchFuture != null) {
            this.watchFuture.cancel(true);
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    private static ThreadPoolTaskScheduler getTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public static class NumberElementReloadEventData {

        @Override
        public String toString() {
            return new StringJoiner(", ", NumberElementReloadEventData.class.getSimpleName() + "[", "]")
                    .toString();
        }
    }
}
