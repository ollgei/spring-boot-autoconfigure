package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.rpc;

import net.javacrumbs.shedlock.core.AbstractSimpleLock;
import net.javacrumbs.shedlock.core.LockConfiguration;

class RpcSimpleLock extends AbstractSimpleLock {
    private final RpcLockProvider rpcLockProvider;
    private final String sessionId;

    public RpcSimpleLock(LockConfiguration lockConfiguration,
                         RpcLockProvider rpcLockProvider,
                         String sessionId) {
        super(lockConfiguration);
        this.rpcLockProvider = rpcLockProvider;
        this.sessionId = sessionId;
    }

    @Override
    protected void doUnlock() {
        rpcLockProvider.unlock(sessionId, lockConfiguration);
    }
}
