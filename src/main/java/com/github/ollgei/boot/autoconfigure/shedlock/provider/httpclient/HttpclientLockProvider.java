package com.github.ollgei.boot.autoconfigure.shedlock.provider.httpclient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ollgei.boot.autoconfigure.shedlock.OllgeiShedlockProperties;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

import static net.javacrumbs.shedlock.core.ClockProvider.now;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class HttpclientLockProvider implements LockProvider, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(HttpclientLockProvider.class);
    @SuppressWarnings("all")
    private final ScheduledExecutorService unlockScheduler = Executors.newSingleThreadScheduledExecutor();

    private final Duration minSessionTtl;
    private final ShedlockHttpclient httpclient;
    private final Duration gracefulShutdownInterval;

    public HttpclientLockProvider(ShedlockHttpclient httpclient, OllgeiShedlockProperties ollgeiShedlockProperties) {
        final OllgeiShedlockProperties.Httpclient properties = ollgeiShedlockProperties.getHttpclient();
        this.httpclient = httpclient;
        this.minSessionTtl = properties.getMinSessionTtl();
        this.gracefulShutdownInterval = properties.getGracefulShutdownInterval();
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public Optional<SimpleLock> lock(LockConfiguration lockConfiguration) {
        if (!createSession(lockConfiguration)) {
            return Optional.empty();
        }
        return tryLock(lockConfiguration);
    }

    void unlock(LockConfiguration lockConfiguration) {
        Duration additionalSessionTtl = Duration.between(now(), lockConfiguration.getLockAtLeastUntil());
        if (!additionalSessionTtl.isNegative() && !additionalSessionTtl.isZero()) {
            logger.debug("Lock will still be held for {}", additionalSessionTtl);
            scheduleUnlock(lockConfiguration.getName(), additionalSessionTtl);
        } else {
            destroy(lockConfiguration.getName());
        }
    }

    private boolean createSession(LockConfiguration lockConfiguration) {
        final long ttlInSeconds = Math.max(lockConfiguration.getLockAtMostFor().getSeconds(),
                minSessionTtl.getSeconds());
        final ShedLockSession session = new ShedLockSession();
        session.setName(lockConfiguration.getName());
        session.setLockAtMostFor(Duration.ofSeconds(ttlInSeconds).toString());
        session.setLockAtLeastFor(lockConfiguration.getLockAtLeastFor().toString());
        return httpclient.tryLock(session);
    }

    private Optional<SimpleLock> tryLock(LockConfiguration lockConfiguration) {
        return Optional.of(new HttpclientSimpleLock(lockConfiguration, this));
    }

    private void scheduleUnlock(String name, Duration unlockTime) {
        unlockScheduler.schedule(
                catchExceptions(() -> destroy(name)),
                unlockTime.toMillis(), TimeUnit.MILLISECONDS
        );
    }

    private void destroy(String name) {
        logger.debug("Destroying name {}", name);
        httpclient.unlock(name);
    }

    private Runnable catchExceptions(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                logger.warn("Exception while execution", t);
            }
        };
    }

    @Override
    public void close() {
        unlockScheduler.shutdown();
        try {
            if (!unlockScheduler.awaitTermination(gracefulShutdownInterval.toMillis(), TimeUnit.MILLISECONDS)) {
                unlockScheduler.shutdownNow();
            }
        } catch (InterruptedException ignored) {
        }
    }
}
