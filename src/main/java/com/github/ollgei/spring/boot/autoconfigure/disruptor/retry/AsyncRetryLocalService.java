package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * 保持幂等性.
 * @author zjw
 * @since 1.0.0
 */
public interface AsyncRetryLocalService<T extends AsyncRetryUpstreamResponse, U extends AsyncRetryLocalResponse> {
    /**
     * 上游处理.
     * @param context object
     * @return
     */
    AsyncRetryResult<T> upstream(Object context);
    /**
     * 本地处理.
     * @param context object
     * @param uResponse upstream
     * @return
     */
    AsyncRetryResult<U> invoke(Object context, T uResponse);
    /**
     * 下游处理.
     * @param context object
     * @param lResponse local
     * @param uResponse upstream
     * @return
     */
    AsyncRetryResultEnum downstream(Object context, T uResponse, U lResponse);
    /**
     * 持久化到数据库中.
     * @param context context
     * @return
     */
    void persist(Object context);
    /**
     * 读取状态 {@link AsyncRetryStateEnum}.
     * @param context context
     * @return
     */
    AsyncRetryStateEnum readState(Object context);
    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @return
     */
    int writeState(Object context, int state);
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default int writeUpstreamResponse(Object context, T response) {
        return writeUpstreamResponse(context, response, AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode());
    }
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    int writeUpstreamResponse(Object context, T response, int state);
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default int writeLocalResponse(Object context, U response) {
        return writeLocalResponse(context, response, AsyncRetryStateEnum.LOCAL_SUCCESS.getCode());
    }
    /**
     * 更新local状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    int writeLocalResponse(Object context, U response, int state);

    /**
     * 读取上游返回的对象.
     * @param context object
     * @return
     */
    T readUpstreamResponse(Object context);
    /**
     * 读取本地处理返回的对象.
     * @param context object
     * @return
     */
    U readLocalResponse(Object context);

}
