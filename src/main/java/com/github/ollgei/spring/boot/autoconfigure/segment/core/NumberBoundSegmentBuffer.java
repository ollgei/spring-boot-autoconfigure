package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 *
 * @author zhangjiawei
 * @since 1.0.0
 */
@Slf4j
public class NumberBoundSegmentBuffer extends BoundSegmentBuffer<NumberElementSection> {

    private final static int INVALID_VALUE = -1;

    private final BoundSegmentRepository repository;

    private final Executor executor;

    public NumberBoundSegmentBuffer(BoundSegmentProperties properties, BoundSegmentRepository repository, Executor executor) {
        super(properties);
        this.repository = repository;
        this.executor = executor;
    }

    @Override
    public long getNumberValue(String name) {
        while (true) {
            getLock().readLock().lock();
            try {
                final long value = tryGetValue(name);
                if (value != INVALID_VALUE) {
                    return value;
                }
            } finally {
                getLock().readLock().unlock();
            }

            final BoundSegment<NumberElementSection> segment = get(name);
            final RuntimeSegment runtime = segment.getRuntime();
            runtime.waitAndSleep();

            getLock().writeLock().lock();
            try {
                final NumberElementSection element = getObject(name);
                long value = element.getValue().getAndIncrement();
                if (value < element.getMax()) {
                    return value;
                }
                if (runtime.isNextReady()) {
                    segment.switchPosition();
                    runtime.setNextReady(false);
                } else {
                    log.error("All segments are not ready!");
                    return INVALID_VALUE;
                }
            } finally {
                getLock().writeLock().unlock();
            }
        }
    }

    public long tryGetValue(final String name) {
        final BoundSegment<NumberElementSection> segment = get(name);
        if (Objects.isNull(segment)) {
            return INVALID_VALUE;
        }
        final NumberElementSection element = getObject(name);
        final RuntimeSegment runtime = segment.getRuntime();
        final AtomicLong value = element.getValue();
        final long idle = element.getMax() - value.get();
        if (!runtime.isNextReady()
                && (idle < properties.getLoaderFactor() * element.getStep())
                && runtime.getInitializingNext().compareAndSet(false, true)) {
            //需要初始化下一个Section
            CompletableFuture.runAsync(() -> {
                boolean inited = false;
                try {
                    loadNextObject(name);
                    inited = true;
                } catch (Exception ex) {
                    log.warn("{} loadNextObject exception: {}", name, ex.getMessage());
                } finally {
                    if (inited) {
                        getLock().writeLock().lock();
                        runtime.setNextReady(true);
                        runtime.getInitializingNext().set(false);
                        getLock().writeLock().unlock();
                    } else {
                        runtime.getInitializingNext().set(false);
                    }
                }
            }, executor);
        }
        final long number = value.getAndIncrement();
        if (number < element.getMax()) {
            return number;
        }
        return INVALID_VALUE;
    }

    @Override
    public void reload(boolean init) {
        final List<SectionDefination> definations = repository.list();
        final List<String> tags = new ArrayList<>();
        //查看所有的LIST
        for (SectionDefination defination : definations) {
            String name = defination.getName();
            tags.add(name);

            SectionDefination definationForUpdate = new SectionDefination();
            definationForUpdate.setName(defination.getName());
            definationForUpdate.setStep(defination.getStep());
            //更新数据库
            if (repository.updateMaxAndStep(definationForUpdate) != null) {
                final BoundSegment<NumberElementSection> segment = BoundSegment.builder().
                        key(name).capacity(2).step(defination.getStep()).build();

                final NumberElementSection element = new NumberElementSection();
                element.setSegment(segment);
                element.getValue().set(defination.getMax() - defination.getStep());
                element.setMax(defination.getMax());
                element.setStep(defination.getStep());
                putIfAbsent(name, segment, element);
            }

        }
        if (!init) {
            removeAll(tags);
        }
    }

    private void loadNextObject(String name) {
        final BoundSegment<NumberElementSection> segment = get(name);
        final RuntimeSegment runtime = segment.getRuntime();

        //动态伸缩step
        final int step = calcStep(name);

        //更新数据库（step）,最大值增加,步长可能增加
        SectionDefination definationForUpdate = new SectionDefination();
        definationForUpdate.setName(name);
        definationForUpdate.setStep(step);
        final SectionDefination defination = repository.updateMaxAndStep(definationForUpdate);
        final NumberElementSection element = new NumberElementSection();
        element.setSegment(segment);
        element.getValue().set(defination.getMax() - step);
        element.setMax(defination.getMax());
        element.setStep(step);
        putNextObject(name, element);

        runtime.setUpdateTimestamp(System.currentTimeMillis());
        runtime.setStep(step);
        runtime.setMinStep(defination.getStep());
    }

    private int calcStep(String name) {
        final BoundSegment<NumberElementSection> segment = get(name);
        final RuntimeSegment runtime = segment.getRuntime();
        final long lastTimestamp = runtime.getUpdateTimestamp();
        if (lastTimestamp == 0) {
            //和现在的步长一致
            return runtime.getStep();
        }
        int step = runtime.getStep();
        int doubleStep = 2 * step;
        int halfStep = step / 2;
        long duration = System.currentTimeMillis() - lastTimestamp;
        if (duration < properties.getDuration()) {
            if (doubleStep < properties.getMaxStep()) {
                return doubleStep;
            }
        } else if (duration >= (properties.getDuration() * 2)) {
            if (halfStep >= runtime.getMinStep()) {
                return halfStep;
            }
        }
        return step;
    }

}
