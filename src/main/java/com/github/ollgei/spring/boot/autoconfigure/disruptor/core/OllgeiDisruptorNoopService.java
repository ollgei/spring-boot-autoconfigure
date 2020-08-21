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
    public void run(Object context) {

    }
}
