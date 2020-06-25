package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

/**
 * 元素段.
 * @author zhangjiawei
 * @since 1.0.0
 */
@Data
class NumberElementSection {
    private AtomicLong value = new AtomicLong(0L);
    private long max;
    private int step;
    private BoundSegment segment;
}
