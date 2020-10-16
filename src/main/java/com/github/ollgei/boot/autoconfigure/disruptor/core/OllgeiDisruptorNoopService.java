package com.github.ollgei.boot.autoconfigure.disruptor.core;

import java.util.concurrent.CountDownLatch;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class OllgeiDisruptorNoopService implements OllgeiDisruptorService {

    @Override
    public void publish(OllgeiDisruptorContext context, CountDownLatch countDownLatch) {

    }

    @Override
    public void write(OllgeiDisruptorContext context) {

    }

    @Override
    public void read(OllgeiDisruptorContext context, CountDownLatch countDownLatch) {

    }
}
