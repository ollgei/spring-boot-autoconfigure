package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import java.io.Serializable;

/**
 * subcription.
 *
 * @author jiawei
 * @since 1.0.0
 */
public abstract class AbstractSubcription implements Serializable {
    private static final long serialVersionUID = 447297790729413407L;
    private long sequence;

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
