package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author jiawei
 * @since 1.0.0
 */
public class DisruptorSubcription implements Serializable {
    private static final long serialVersionUID = -2449741731766907208L;
    private long sequence;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
