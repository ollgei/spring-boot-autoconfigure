package com.github.ollgei.spring.boot.autoconfigure.shedlock.provider.feign;

import net.javacrumbs.shedlock.support.StorageBasedLockProvider;

/**
 * mybatis lock provider.
 * @author ollgei
 * @since 1.0.0
 */
public class FeignLockProvider extends StorageBasedLockProvider {

    public FeignLockProvider(FeignStorageAccessor restStorageAccessor) {
        super(restStorageAccessor);
    }

}
