package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

/**
 * publisher
 *
 * @author jiawei
 * @since 1.0.0
 */
@Slf4j
public class DisruptorPublisher implements InitializingBean, DisposableBean {

    private final ExecutorService executorService;

    private final Disruptor<InternalEvent> disruptor;

    private final long coolingDownPeriod;
    private volatile boolean started = true;
    private volatile boolean disruptorShutDown = false;

    public static Builder builder() {
        return new Builder();
    }

    protected DisruptorPublisher(Builder builder) {

        builder.validate();

        Executor executor = builder.executor;
        if (executor == null) {
            //优化
            int threadSize = builder.subscriberCount * 2;
            executorService = new ThreadPoolExecutor(threadSize, threadSize, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new InternalNamedThreadFactory(builder.subscriberName),
                    new ThreadPoolExecutor.AbortPolicy());
            executor = executorService;
        } else {
            executorService = null;
        }

        coolingDownPeriod = builder.coolingDownPeriod;

        disruptor = new Disruptor<>(() -> new InternalEvent(),
                builder.bufferSize,
                new InternalNamedThreadFactory(builder.subscriberName),
                builder.producerType,
                builder.waitStrategy);

        // Configure invoker Threads
        final InternalWorkerHandler[] handlers = new InternalWorkerHandler[builder.subscriberCount];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new InternalWorkerHandler(executor, builder.subscriber);
        }

        disruptor.setDefaultExceptionHandler(new InternalExceptionHandler(disruptor));
        disruptor.handleEventsWithWorkerPool(handlers);
    }

    public void stop() {
        if (!started) {
            return;
        }
        started = false;
        long lastChangeDetected = System.currentTimeMillis();
        long lastKnownCursor = disruptor.getRingBuffer().getCursor();
        while (System.currentTimeMillis() - lastChangeDetected < coolingDownPeriod && !Thread.interrupted()) {
            if (disruptor.getRingBuffer().getCursor() != lastKnownCursor) {
                lastChangeDetected = System.currentTimeMillis();
                lastKnownCursor = disruptor.getRingBuffer().getCursor();
            }
        }
        disruptorShutDown = true;
        disruptor.shutdown();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        disruptor.start();
        log.info("started disruptor.....");
    }

    /**
     * send disruptor data.
     *
     * @param subcription subcription.
     */
    public void write(final AbstractSubcription subcription) {
        if (disruptorShutDown) {
            throw new IllegalStateException("Disruptor has been shut down. Cannot send data");
        }
        final RingBuffer<InternalEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new InternalEventTranslator(), subcription);
    }

    /**
     * send disruptor data.
     *
     * @param subcription subcription.
     */
    public synchronized void singleWrite(final AbstractSubcription subcription) {
        write(subcription);
    }

    public static class Builder {

        private static final int DEFAULT_BUFFER_SIZE = 4096;

        private Executor executor;
        private String subscriberName = "disruptor";
        private long coolingDownPeriod = 1000;
        private int bufferSize = DEFAULT_BUFFER_SIZE;
        private ProducerType producerType = ProducerType.MULTI;
        private WaitStrategy waitStrategy = new BlockingWaitStrategy();
        private int subscriberCount = 16;
        private DisruptorSubscriber subscriber = subcription -> {
            throw new RuntimeException("Not Config Asynchronous Conusmer!!");
        };
        private boolean autoDestroy = true;
        private boolean globalQueue = true;

        public Builder setGlobalQueue(boolean globalQueue) {
            this.globalQueue = globalQueue;
            return this;
        }

        public Builder setAutoDestroy(boolean autoDestroy) {
            this.autoDestroy = autoDestroy;
            return this;
        }

        public Builder setSubscriberCount(int subscriberCount) {
            this.subscriberCount = subscriberCount;
            return this;
        }

        public Builder setSubscriberName(String subscriberName) {
            this.subscriberName = subscriberName;
            return this;
        }

        public Builder setCoolingDownPeriod(long coolingDownPeriod) {
            this.coolingDownPeriod = coolingDownPeriod;
            return this;
        }

        public Builder setExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder setProducerType(ProducerType producerType) {
            this.producerType = producerType;
            return this;
        }

        public Builder setWaitStrategy(WaitStrategy waitStrategy) {
            this.waitStrategy = waitStrategy;
            return this;
        }

        public Builder setSubscriber(DisruptorSubscriber subscriber) {
            this.subscriber = subscriber;
            return this;
        }

        /**
         * Initializes a {@link DisruptorPublisher} as specified through this Builder.
         *
         * @return a {@link DisruptorPublisher} as specified through this Builder
         */
        public DisruptorPublisher build() {
            return new DisruptorPublisher(this);
        }

        /**
         * Validate whether the fields contained in this Builder as set accordingly.
         *
         * @throws if one field is asserted to be incorrect according to the Builder's
         *                                    specifications
         */
        protected void validate() {

        }
    }

}
