package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class OllgeiDisruptorNoopService implements OllgeiDisruptorService {

    @Override
    public String kind() {
        return null;
    }

    @Override
    public void publish(OllgeiDisruptorContext context) {

    }

    @Override
    public void write(OllgeiDisruptorContext context) {

    }

    @Override
    public void read(OllgeiDisruptorContext context) {

    }
}