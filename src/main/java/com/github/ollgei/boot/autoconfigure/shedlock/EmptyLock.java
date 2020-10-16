package com.github.ollgei.boot.autoconfigure.shedlock;

import net.javacrumbs.shedlock.core.SimpleLock;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class EmptyLock implements SimpleLock {
    @Override
    public void unlock() {

    }
}
