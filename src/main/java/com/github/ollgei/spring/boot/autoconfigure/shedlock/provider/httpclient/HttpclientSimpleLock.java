package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.httpclient;

import net.javacrumbs.shedlock.core.AbstractSimpleLock;
import net.javacrumbs.shedlock.core.LockConfiguration;

class HttpclientSimpleLock extends AbstractSimpleLock {
    private final HttpclientLockProvider httpclientLockProvider;

    public HttpclientSimpleLock(LockConfiguration lockConfiguration,
                                HttpclientLockProvider httpclientLockProvider) {
        super(lockConfiguration);
        this.httpclientLockProvider = httpclientLockProvider;
    }

    @Override
    protected void doUnlock() {
        httpclientLockProvider.unlock(lockConfiguration);
    }
}
