package com.github.ollgei.boot.autoconfigure.disruptor.retryablex;

import java.util.concurrent.CountDownLatch;

import lombok.Data;
import lombok.ToString;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
@ToString(callSuper = true)
public class RetryableSyncTopic extends RetryableTopic {
    /**
     * countDownLatch.
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

}
