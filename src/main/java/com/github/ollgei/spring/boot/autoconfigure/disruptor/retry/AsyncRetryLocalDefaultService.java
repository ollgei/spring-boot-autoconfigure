package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * 保持幂等性.
 * @author zjw
 * @since 1.0.0
 */
public interface AsyncRetryLocalDefaultService<T extends AsyncRetryUpstreamResponse, U extends AsyncRetryLocalResponse> extends AsyncRetryLocalService<T, U> {
}
