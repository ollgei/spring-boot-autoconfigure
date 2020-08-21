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

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class DefaultOllgeiShedLockComponent implements OllgeiShedLockComponent {

    private LockingTaskExecutor executor;

    private OllgeiShedlockProperties properties;

    public DefaultOllgeiShedLockComponent(OllgeiShedlockProperties ollgeiShedlockProperties, LockProvider lockProvider) {
        this.properties = ollgeiShedlockProperties;
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
    }

    @Override
    public void execute(String name, Runnable runnable) {
        try {
            executor.executeWithLock(runnable,
                    new LockConfiguration(ClockProvider.now(), name,
                            properties.getLockMostFor(),
                            properties.getLockLeastFor()));
        } catch (Throwable throwable) {
            log.warn("lock error {}", throwable.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_LOCK, throwable);
        }
    }

    @Override
    public <T> T execute(String name, Callable<T> callable) {
        try {
            final T result = executor.executeWithLock(() -> callable.call(),
                    new LockConfiguration(ClockProvider.now(), name,
                            properties.getLockMostFor(),
                            properties.getLockLeastFor())).getResult();
            return result;
        } catch (Throwable throwable) {
            log.warn("lock error {}", throwable.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_LOCK, throwable);
        }
    }

}
