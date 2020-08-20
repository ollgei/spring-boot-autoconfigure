package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * 保持幂等性.
 * @author zjw
 * @since 1.0.0
 */
public interface AsyncRetryLocalService {
    /**
     * 上游处理.
     * @param object object
     * @return
     */
    <T extends AsyncRetryUpstreamResult> T upstream(AsyncRetryObject object);
    /**
     * 上游处理.
     * @param object object
     * @param uResult upstream
     * @return
     */
    <T extends AsyncRetryLocalResult> T invoke(AsyncRetryObject object, AsyncRetryUpstreamResult uResult);
    /**
     * 通知给下游.
     * @param object object
     * @param lResult local
     * @param uResult upstream
     * @return
     */
    AsyncRetryResult downstream(AsyncRetryObject object, AsyncRetryUpstreamResult uResult, AsyncRetryLocalResult lResult);

}
