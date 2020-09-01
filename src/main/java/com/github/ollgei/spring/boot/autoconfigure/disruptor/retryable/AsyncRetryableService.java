package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 * @author ollgei
 * @since 1.0.0
 */
public interface AsyncRetryableService<C extends AsyncRetryableContext, T extends AsyncRetryableUpstreamResponse, U extends AsyncRetryableMidstreamResponse, S extends AsyncRetryableDownstreamResponse> {
    /**
     * 上游处理.
     * @param context object
     * @return
     */
    T upstream(C context);
    /**
     * 中游处理.
     * @param context object
     * @param uResponse upstream
     * @return
     */
    U midstream(C context, T uResponse);
    /**
     * 下游处理.
     * @param context object
     * @param uResponse upstream
     * @return
     */
    S downstream(C context, T uResponse, U mResponse);
    /**
     * 读取状态 {@link AsyncRetryableStateEnum}.
     * @param context context
     * @return
     */
    AsyncRetryableStateEnum readState(C context);
    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @return
     */
    default void writeState(C context, int state) {
        writeState(context, state, false);
    }

    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @param success 是否成功
     * @return
     */
    void writeState(C context, int state, boolean success);
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default void writeUpstreamResponse(C context, T response) {
        writeUpstreamResponse(context, response, AsyncRetryableStateEnum.UPSTREAM_SUCCESS.getCode());
    }
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    void writeUpstreamResponse(C context, T response, int state);
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default void writeMidstreamResponse(C context, U response) {
        writeMidstreamResponse(context, response, AsyncRetryableStateEnum.MIDSTREAM_SUCCESS.getCode());
    }
    /**
     * 更新midstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    void writeMidstreamResponse(C context, U response, int state);

    /**
     * 更新downstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default void writeDownstreamResponse(C context, S response) {
        writeDownstreamResponse(context, response, AsyncRetryableStateEnum.DOWNSTREAM_SUCCESS.getCode());
    }
    /**
     * 更新downstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    void writeDownstreamResponse(C context, S response, int state);

    /**
     * 读取上游返回的对象.
     * @param context object
     * @return
     */
    T readUpstreamResponse(C context);
    /**
     * 读取本地处理返回的对象.
     * @param context object
     * @return
     */
    U readMidstreamResponse(C context);
    /**
     * 读取下游处理返回的对象.
     * @param context object
     * @return
     */
    S readDownstreamResponse(C context);
}
