package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.springframework.util.Assert;

/**
 * 有边界的段.
 *
 * @author zhangjiawei
 * @since 1.0.0
 */
public class BoundSegment<E> {

    private final String key;
    private final int capacity;
    private final AtomicReferenceArray<E> array;
    private final RuntimeSegment runtime;


    BoundSegment(final String key, final int capacity, final int step) {
        Assert.notNull(key, "key is not null");
        this.key = key;
        this.capacity = capacity;
        this.array = new AtomicReferenceArray<>(capacity);
        this.runtime = initRuntime(step);
    }

    public static BoundSegment.BoundSegmentBuilder builder() {
        return new BoundSegment.BoundSegmentBuilder();
    }

    public int calcPosition() {
        return runtime.getIndex() % this.capacity;
    }

    public int calcNextPosition() {
        return runtime.nextIndex() % this.capacity;
    }

    private RuntimeSegment initRuntime(int step) {
        final RuntimeSegment running = new RuntimeSegment();
        running.setIndex(0);
        running.setNextReady(false);
        running.setUpdateTimestamp(0);
        running.setStep(step);
        running.setMinStep(step);
        return running;
    }

    public int switchPosition() {
        runtime.incrIndex();
        return calcPosition();
    }

    public void putObject(int i, E value) {
        this.array.set(i, value);
    }

    public String getKey() {
        return key;
    }

    public int getCapacity() {
        return capacity;
    }

    public AtomicReferenceArray<E> getArray() {
        return array;
    }

    public RuntimeSegment getRuntime() {
        return runtime;
    }


    public static class BoundSegmentBuilder {
        private String key;
        private int capacity = 2;
        private int step;

        BoundSegmentBuilder() {
        }

        public BoundSegment.BoundSegmentBuilder key(final String key) {
            this.key = key;
            return this;
        }

        public BoundSegment.BoundSegmentBuilder capacity(final int capacity) {
            this.capacity = capacity;
            return this;
        }

        public BoundSegment.BoundSegmentBuilder step(final int step) {
            this.step = step;
            return this;
        }

        public BoundSegment build() {
            return new BoundSegment(this.key, this.capacity, this.step);
        }

    }
}