package com.github.ollgei.boot.autoconfigure.shedlock.provider.mybatis;

import net.javacrumbs.shedlock.support.StorageBasedLockProvider;

/**
 * mybatis lock provider.
 * @author ollgei
 * @since 1.0.0
 */
public class MybatisLockProvider extends StorageBasedLockProvider {

    public MybatisLockProvider(MybatisStorageAccessor mybatisStorageAccessor) {
        super(mybatisStorageAccessor);
    }

}
