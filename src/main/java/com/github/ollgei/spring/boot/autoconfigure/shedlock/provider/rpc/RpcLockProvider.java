package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.rpc;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

import static net.javacrumbs.shedlock.core.ClockProvider.now;

/**
 * TODO: rpc lock provider.
 *
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class RpcLockProvider implements LockProvider, AutoCloseable {
    private static final String DEFAULT_CONSUL_LOCK_POSTFIX = "-leader";
    private static final Duration DEFAULT_GRACEFUL_SHUTDOWN_INTERVAL = Duration.ofSeconds(2);
    @SuppressWarnings("all")
    private final ScheduledExecutorService unlockScheduler = Executors.newSingleThreadScheduledExecutor();

    private final Duration minSessionTtl;
    private final String consulLockPostfix;
    private final RpcShedlockClient rpcShedlockClient;
    private final Duration gracefulShutdownInterval;

    public RpcLockProvider(RpcShedlockClient rpcShedlockClient) {
        this(rpcShedlockClient, Duration.ofSeconds(10), DEFAULT_CONSUL_LOCK_POSTFIX, DEFAULT_GRACEFUL_SHUTDOWN_INTERVAL);
    }

    public RpcLockProvider(RpcShedlockClient rpcShedlockClient, Duration minSessionTtl) {
        this(rpcShedlockClient, minSessionTtl, DEFAULT_CONSUL_LOCK_POSTFIX, DEFAULT_GRACEFUL_SHUTDOWN_INTERVAL);
    }

    public RpcLockProvider(RpcShedlockClient rpcShedlockClient, Duration minSessionTtl, String consulLockPostfix, Duration gracefulShutdownInterval) {
        this.rpcShedlockClient = rpcShedlockClient;
        this.consulLockPostfix = consulLockPostfix;
        this.minSessionTtl = minSessionTtl;
        this.gracefulShutdownInterval = gracefulShutdownInterval;
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public Optional<SimpleLock> lock(LockConfiguration lockConfiguration) {
        String sessionId = createSession(lockConfiguration);
        return tryLock(sessionId, lockConfiguration);
    }

    void unlock(String sessionId, LockConfiguration lockConfiguration) {
        Duration additionalSessionTtl = Duration.between(now(), lockConfiguration.getLockAtLeastUntil());
        if (!additionalSessionTtl.isNegative() && !additionalSessionTtl.isZero()) {
            log.debug("Lock will still be held for {}", additionalSessionTtl);
            scheduleUnlock(sessionId, additionalSessionTtl);
        } else {
            destroy(sessionId);
        }
    }

    private String getLeaderKey(LockConfiguration lockConfiguration) {
        return lockConfiguration.getName() + consulLockPostfix;
    }

    private void scheduleUnlock(String sessionId, Duration unlockTime) {
        unlockScheduler.schedule(
                catchExceptions(() -> destroy(sessionId)),
                unlockTime.toMillis(), TimeUnit.MILLISECONDS
        );
    }

    private void destroy(String sessionId) {
        log.debug("Destroying session {}", sessionId);
//        consulClient.sessionDestroy(sessionId, QueryParams.DEFAULT);
    }

    private Runnable catchExceptions(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                log.warn("Exception while execution", t);
            }
        };
    }

    private String createSession(LockConfiguration lockConfiguration) {
        long ttlInSeconds = Math.max(lockConfiguration.getLockAtMostFor().getSeconds(), minSessionTtl.getSeconds());
//        NewSession newSession = new NewSession();
//        newSession.setName(lockConfiguration.getName());
//        newSession.setLockDelay(0);
//        newSession.setBehavior(Session.Behavior.DELETE);
//        newSession.setTtl(ttlInSeconds + "s");
        String sessionId = "";//consulClient.sessionCreate(newSession, QueryParams.DEFAULT).getValue();
        log.debug("Acquired session {} for {} seconds", sessionId, ttlInSeconds);
        return sessionId;
    }

    private Optional<SimpleLock> tryLock(String sessionId, LockConfiguration lockConfiguration) {
//        PutParams putParams = new PutParams();
//        putParams.setAcquireSession(sessionId);
        String leaderKey = getLeaderKey(lockConfiguration);
        boolean isLockSuccessful = true;//consulClient.setKVValue(leaderKey, lockConfiguration.getName(), putParams).getValue();
        if (isLockSuccessful) {
            return Optional.of(new RpcSimpleLock(lockConfiguration, this, sessionId));
        }
        return Optional.empty();
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
