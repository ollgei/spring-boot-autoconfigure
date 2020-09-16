package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

import net.javacrumbs.shedlock.core.AbstractSimpleLock;
import net.javacrumbs.shedlock.core.LockConfiguration;

class HttpclientSimpleLock extends AbstractSimpleLock {
    private final HttpclientLockProvider httpclientLockProvider;
    private final String sessionId;

    public HttpclientSimpleLock(LockConfiguration lockConfiguration,
                                HttpclientLockProvider httpclientLockProvider,
                                String sessionId) {
        super(lockConfiguration);
        this.httpclientLockProvider = httpclientLockProvider;
        this.sessionId = sessionId;
    }

    @Override
    protected void doUnlock() {
        httpclientLockProvider.unlock(sessionId, lockConfiguration);
    }
}
