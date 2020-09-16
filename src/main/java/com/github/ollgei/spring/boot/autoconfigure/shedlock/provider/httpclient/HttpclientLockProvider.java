package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.github.ollgei.spring.boot.autoconfigure.shedlock.OllgeiShedlockProperties;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import net.javacrumbs.shedlock.provider.consul.ConsulLockProvider;
import net.javacrumbs.shedlock.support.annotation.NonNull;

import static net.javacrumbs.shedlock.core.ClockProvider.now;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class HttpclientLockProvider implements LockProvider, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ConsulLockProvider.class);
    private static final Duration DEFAULT_GRACEFUL_SHUTDOWN_INTERVAL = Duration.ofSeconds(2);
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
    @NonNull
    public Optional<SimpleLock> lock(@NonNull LockConfiguration lockConfiguration) {
        String sessionId = createSession(lockConfiguration);
        if (StringUtils.hasText(sessionId)) {
            return Optional.empty();
        }
        return tryLock(sessionId, lockConfiguration);
    }

    void unlock(String sessionId, LockConfiguration lockConfiguration) {
        Duration additionalSessionTtl = Duration.between(now(), lockConfiguration.getLockAtLeastUntil());
        if (!additionalSessionTtl.isNegative() && !additionalSessionTtl.isZero()) {
            logger.debug("Lock will still be held for {}", additionalSessionTtl);
            scheduleUnlock(sessionId, additionalSessionTtl);
        } else {
            destroy(sessionId);
        }
    }

    private String createSession(LockConfiguration lockConfiguration) {
        long ttlInSeconds = Math.max(lockConfiguration.getLockAtMostFor().getSeconds(), minSessionTtl.getSeconds());
        final ShedLockSession object = new ShedLockSession();
        object.setName(lockConfiguration.getName());
        object.setLockAtMostFor(lockConfiguration.getLockAtMostFor());
        object.setLockAtLeastFor(lockConfiguration.getLockAtLeastFor());
        String sessionId = httpclient.tryLock(object);
        logger.debug("Acquired session {} for {} seconds", sessionId, ttlInSeconds);
        return sessionId;
    }

    private Optional<SimpleLock> tryLock(String sessionId, LockConfiguration lockConfiguration) {
        return Optional.of(new HttpclientSimpleLock(lockConfiguration, this, sessionId));
    }

    private void scheduleUnlock(String sessionId, Duration unlockTime) {
        unlockScheduler.schedule(
                catchExceptions(() -> destroy(sessionId)),
                unlockTime.toMillis(), TimeUnit.MILLISECONDS
        );
    }

    private void destroy(String sessionId) {
        logger.debug("Destroying sessionId {}", sessionId);
        httpclient.unlock(sessionId);
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
