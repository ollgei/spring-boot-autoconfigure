package com.github.ollgei.spring.boot.autoconfigure.retry;

import org.springframework.retry.RetryContext;

/**
 * repository.
 * @author ollgei
 * @since 1.0.0
 */
public interface RetryRepository {

    /**
     * save error.
     * @param context context
     * @return
     */
    void registerThrowable(RetryContext context);

}
