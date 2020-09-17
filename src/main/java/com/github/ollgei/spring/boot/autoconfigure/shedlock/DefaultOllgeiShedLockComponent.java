package com.github.ollgei.spring.boot.autoconfigure.shedlock;

import java.util.concurrent.Callable;

import com.github.ollgei.base.commonj.errors.ErrorCodeEnum;
import com.github.ollgei.base.commonj.errors.ErrorException;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.ClockProvider;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.core.SimpleLock;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class DefaultOllgeiShedLockComponent implements OllgeiShedLockComponent {

    private LockingTaskExecutor executor;

    private LockProvider lockProvider;

    private OllgeiShedlockProperties properties;

    public DefaultOllgeiShedLockComponent(OllgeiShedlockProperties ollgeiShedlockProperties, LockProvider lockProvider) {
        this.properties = ollgeiShedlockProperties;
        this.lockProvider = lockProvider;
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
    }

    @Override
    public SimpleLock lock(LockConfiguration lockConfiguration) {
        return lockProvider.lock(lockConfiguration).orElseGet(EmptyLock::new);
    }

    @Override
    public void unlock(SimpleLock lock) {
        lock.unlock();
    }

    @Override
    public void execute(LockConfiguration lockConfiguration, Runnable runnable) {
        try {
            executor.executeWithLock(runnable, lockConfiguration);
        } catch (Throwable throwable) {
            log.warn("lock error {}", throwable.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_LOCK, throwable);
        }
    }

    @Override
    public <T> T execute(LockConfiguration lockConfiguration, Callable<T> callable) {
        try {
            final T result = executor.executeWithLock(() -> callable.call(), lockConfiguration).getResult();
            return result;
        } catch (Throwable throwable) {
            log.warn("lock error {}", throwable.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_LOCK, throwable);
        }
    }

    @Override
    public void execute(String name, Runnable runnable) {
        execute(new LockConfiguration(ClockProvider.now(), name,
                properties.getLockMostFor(),
                properties.getLockLeastFor()), runnable);
    }

    @Override
    public <T> T execute(String name, Callable<T> callable) {
       return execute(new LockConfiguration(ClockProvider.now(), name,
                properties.getLockMostFor(),
                properties.getLockLeastFor()), callable);
    }
}
