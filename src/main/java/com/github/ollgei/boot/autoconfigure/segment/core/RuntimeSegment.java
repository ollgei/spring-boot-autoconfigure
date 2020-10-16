package com.github.ollgei.boot.autoconfigure.segment.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Data;

/**
 * 运行时.
 * @author ollgei
 * @since 1.0.0
 */
@Data
class RuntimeSegment {
    volatile int index;
    volatile int step;
    volatile int minStep;
    /** 下个段是否准备完成 */
    volatile boolean nextReady;
    volatile long updateTimestamp;
    /** 正在初始化下一个段 */
    AtomicBoolean initializingNext = new AtomicBoolean(false);

    void incrIndex() {
        ++index;
    }

    int nextIndex() {
        return index + 1;
    }

    void waitAndSleep() {
        int roll = 0;
        while (initializingNext.get()) {
            roll += 1;
            if(roll > 10000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    break;
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
