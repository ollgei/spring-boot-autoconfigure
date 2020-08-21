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
    /**
     * 持久化到数据库中.
     * @param object object
     * @return
     */
    void persist(AsyncRetryObject object);
    /**
     * 读取状态 {@link AsyncRetryStateEnum}.
     * @param object object
     * @return
     */
    AsyncRetryStateEnum readState(AsyncRetryObject object);
    /**
     * 更新upstream状态.
     * @param response response
     * @return
     */
    default int writeUpstreamResponse(T response) {
        return writeUpstreamResponse(response, AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode());
    }

    /**
     * 更新upstream状态.
     * @param response response
     * @param state state
     * @return
     */
    int writeUpstreamResponse(T response, int state);
    /**
     * 更新upstream状态.
     * @param response response
     * @return
     */
    default int writeLocalResponse(U response) {
        return writeLocalResponse(response, AsyncRetryStateEnum.LOCAL_SUCCESS.getCode());
    }
    /**
     * 更新local状态.
     * @param response response
     * @param state state
     * @return
     */
    int writeLocalResponse(U response, int state);
    /**
     * 更新状态.
     * @param state 新状态
     * @return
     */
    int updateState(int state);

    /**
     * 读取上游返回的对象.
     * @param object object
     * @return
     */
    T readUpstreamResponse(AsyncRetryObject object);
    /**
     * 读取本地处理返回的对象.
     * @param object object
     * @return
     */
    U readLocalResponse(AsyncRetryObject object);

}
