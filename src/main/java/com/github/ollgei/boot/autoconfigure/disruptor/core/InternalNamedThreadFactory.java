package com.github.ollgei.boot.autoconfigure.disruptor.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

/**
 * <p>
 * </p>
 *
 * @author jiawei
 * @since 1.0.0
 */
class InternalNamedThreadFactory implements ThreadFactory {

    private final int priority;
    private final ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger();
    private final boolean daemon;

    /**
     * Initializes a ThreadFactory instance that creates each thread in a group with given {@code groupName} with
     * default priority.
     *
     * @param groupName The name of the group to create each thread in
     * @see Thread#setPriority(int)
     */
    public InternalNamedThreadFactory(String groupName) {
        this(new ThreadGroup(groupName), false);
    }

    /**
     * Initializes a ThreadFactory instance that creates each thread in a group with given {@code groupName} with
     * default priority.
     *
     * @param groupName The name of the group to create each thread in
     * @see Thread#setPriority(int)
     */
    public InternalNamedThreadFactory(String groupName, boolean daemon) {
        this(new ThreadGroup(groupName), daemon);
    }

    /**
     * Initializes a ThreadFactory instance that create each thread in the given {@code group} with default
     * priority.
     *
     * @param group The ThreadGroup to create each thread in
     * @see Thread#setPriority(int)
     */
    public InternalNamedThreadFactory(ThreadGroup group, boolean daemon) {
        this(Thread.NORM_PRIORITY, group, daemon);
    }

    /**
     * Initializes a ThreadFactory instance that create each thread in the given {@code group} with given
     * {@code priority}.
     *
     * @param priority The priority of the threads to create
     * @param group    The ThreadGroup to create each thread in
     * @see Thread#setPriority(int)
     */
    public InternalNamedThreadFactory(int priority, ThreadGroup group, boolean daemon) {
        Assert.isTrue(priority <= Thread.MAX_PRIORITY && priority >= Thread.MIN_PRIORITY,
                () -> "Given priority is invalid");
        this.priority = priority;
        this.threadGroup = group;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, threadGroup.getName() + "-" + nextThreadNumber());
        thread.setPriority(priority);
        thread.setDaemon(daemon);
        return thread;
    }

    private int nextThreadNumber() {
        return threadNumber.getAndIncrement();
    }
}
