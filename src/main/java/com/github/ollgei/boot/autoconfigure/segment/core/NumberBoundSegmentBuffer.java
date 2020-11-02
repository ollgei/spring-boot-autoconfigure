package com.github.ollgei.boot.autoconfigure.segment.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import com.github.ollgei.boot.autoconfigure.segment.BoundSegmentProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 *
 * @author ollgei
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
            lockForRead();
            try {
                final long value = tryGetValue(name);
                if (value != INVALID_VALUE) {
                    return value;
                }
            } finally {
                unlockForRead();
            }

            final BoundSegment<NumberElementSection> segment = get(name);
            if (segment == null) {
                return INVALID_VALUE;
            }
            log.info("waitAndSleep start");
            segment.waitAndSleep();
            log.info("waitAndSleep end");

            lockForWrite();
            try {
                final NumberElementSection element = getObject(name);
                long value = element.getValue().getAndIncrement();
                if (value < element.getMax()) {
                    return value;
                }
                final RuntimeSegment runtime = segment.getRuntime();
                if (runtime.isNextReady()) {
                    segment.switchPosition();
                    runtime.setNextReady(false);
                } else {
                    log.error("All segments are not ready!");
                    return INVALID_VALUE;
                }
            } finally {
                unlockForWrite();
            }
        }
    }

    private long tryGetValue(final String name) {
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
                        lockForWrite();
                        try {
                            runtime.setNextReady(true);
                            runtime.getInitializingNext().set(false);
                        } finally {
                            unlockForWrite();
                        }
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
        final List<SectionDefinition> definations = repository.list();
        final List<String> tags = new ArrayList<>();
        //查看所有的LIST
        for (final SectionDefinition defination : definations) {
            final String name = defination.getName();
            tags.add(name);

            //返回为空，不会放入到缓存中
            putIfAbsent(name, k -> {
                if (!name.equals(k)) {
                    return null;
                }
                final SectionDefinition definationForUpdate = new SectionDefinition();
                definationForUpdate.setName(k);
                definationForUpdate.setStep(defination.getStep());
                final SectionDefinition definationNew = repository.updateMaxAndStep(definationForUpdate);
                if (definationNew == null) {
                    return null;
                }

                final BoundSegment<NumberElementSection> segment = BoundSegment.builder().key(k).capacity(2).step(definationNew.getStep()).build();
                final NumberElementSection element = new NumberElementSection();
                element.setSegment(segment);
                element.getValue().set(definationNew.getMax() - definationNew.getStep());
                element.setMax(definationNew.getMax());
                element.setStep(definationNew.getStep());

                segment.putObject(0, element);

                return segment;
            });
        }

        if (!init) {
            removeAll(tags);
        }
    }

    private void loadNextObject(String name) {
        final BoundSegment<NumberElementSection> segment = get(name);
        if (segment == null) {
            return;
        }
        final RuntimeSegment runtime = segment.getRuntime();

        //动态伸缩step
        final int step = calcStep(name);

        //更新数据库（step）,最大值增加,步长可能增加
        final SectionDefinition definationForUpdate = new SectionDefinition();
        definationForUpdate.setName(name);
        definationForUpdate.setStep(step);
        final SectionDefinition definition = repository.updateMaxAndStep(definationForUpdate);
        final NumberElementSection element = new NumberElementSection();
        element.setSegment(segment);
        element.getValue().set(definition.getMax() - step);
        element.setMax(definition.getMax());
        element.setStep(step);
        putNextObject(name, element);

        log.info("step:{}, minStep:{}", step, definition.getStep());
        runtime.setUpdateTimestamp(System.currentTimeMillis());
        runtime.setStep(step);
        runtime.setMinStep(definition.getStep());
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
