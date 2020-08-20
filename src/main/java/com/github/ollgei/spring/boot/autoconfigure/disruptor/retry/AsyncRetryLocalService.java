package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * 保持幂等性.
 * @author zjw
 * @since 1.0.0
 */
public interface AsyncRetryLocalService<T extends AsyncRetryUpstreamResponse, U extends AsyncRetryLocalResponse> {
    /**
     * 上游处理.
     * @param object object
     * @return
     */
    AsyncRetryResult<T> upstream(AsyncRetryObject object);
    /**
     * 上游处理.
     * @param object object
     * @param uResponse upstream
     * @return
     */
    AsyncRetryResult<U> invoke(AsyncRetryObject object, T uResponse);
    /**
     * 通知给下游.
     * @param object object
     * @param lResponse local
     * @param uResponse upstream
     * @return
     */
    AsyncRetryResultEnum downstream(AsyncRetryObject object, T uResponse, U lResponse);

}
