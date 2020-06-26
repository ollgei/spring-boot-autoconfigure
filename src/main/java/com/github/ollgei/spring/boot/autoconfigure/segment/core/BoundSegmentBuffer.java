package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import org.springframework.beans.factory.InitializingBean;

import com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties;

/**
 * 有边界的段缓存.
 * 1 事件监听机制 定时更新
 * 2 动态伸缩 step （判断消费到10%的时候，所用的时间，来决定是否更新数据库的step）
 * @author zhangjiawei
 * @since 1.0.0
 */
public abstract class BoundSegmentBuffer<E> implements InitializingBean {

    private final Map<String, BoundSegment<E>> segments;
    private final ReadWriteLock lock;
    protected BoundSegmentProperties properties;

    public BoundSegmentBuffer(BoundSegmentProperties properties) {
        this.properties = properties;
        this.segments = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void afterPropertiesSet() {
        safeReload(true);
    }

    public synchronized void safeReload(boolean init) {
        this.reload(init);
    }

    protected void putIfAbsent(String key, Function<String, BoundSegment<E>> mappingFunction) {
        //创建新的 旧的不用管 因为会定时经常拉去
        this.segments.computeIfAbsent(key, k -> mappingFunction.apply(k));
    }

    protected void removeAll(List<String> keys) {
        List<String> tags = new ArrayList<>();
        for (Map.Entry<String, BoundSegment<E>> entry : this.segments.entrySet()) {
            String key = entry.getKey();
            if (keys.contains(key)) {
                tags.add(key);
            }
        }
        tags.stream().filter(s -> !keys.contains(s)).forEach(s -> this.segments.remove(s));
    }

    protected BoundSegment<E> get(String key) {
        return this.segments.get(key);
    }


    protected Map<String, BoundSegment<E>> getSegments() {
        return segments;
    }

    protected ReadWriteLock getLock() {
        return lock;
    }

    protected void putNextObject(String name, E value) {
        final BoundSegment segment = get(name);
        segment.getArray().set(segment.calcNextPosition(), value);
    }

    protected void putObject(String name, E value) {
        final BoundSegment segment = get(name);
        segment.getArray().set(segment.calcPosition(), value);
    }

    protected E getObject(String name) {
        final BoundSegment segment = get(name);
        return (E) segment.getArray().get(segment.calcPosition());
    }

    abstract public long getNumberValue(String name);
    abstract public void reload(boolean init);
}
