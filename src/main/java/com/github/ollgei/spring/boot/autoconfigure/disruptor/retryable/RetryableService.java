package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * 异步重试.
 * @author ollgei
 * @since 1.0.0
 */
public interface RetryableService<T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse> {
    /**
     * 上游处理.
     * @param context object
     * @return
     */
    T upstream(RetryableContext context);
    /**
     * 中游处理.
     * @param context object
     * @param uResponse upstream
     * @return
     */
    U midstream(RetryableContext context, T uResponse);
    /**
     * 下游处理.
     * @param context object
     * @param uResponse upstream
     * @return
     */
    S downstream(RetryableContext context, T uResponse, U mResponse);

    /**
     * 读取状态 {@link RetryableStateEnum}.
     * @param context context
     * @return
     */
    RetryableStateEnum readState(RetryableContext context);
    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @return
     */
    default void writeState(RetryableContext context, int state) {
        writeState(context, state, false);
    }

    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @param success 是否成功
     * @return
     */
    void writeState(RetryableContext context, int state, boolean success);
    /**
     * 更新upstream状态.
     * @param context object
     * @param uResponse upstream response
     * @param mResponse midstream response
     * @param dResponse downstream response
     * @param state state
     * @return
     */
    void writeResponse(RetryableContext context, T uResponse, U mResponse, S dResponse, int state);

    /**
     * 更新upstream状态.
     * @param context object
     * @param response upstream response
     * @param state state
     * @return
     */
    default void writeUpstreamResponse(RetryableContext context, T response, int state) {
        writeResponse(context, response, null, null, state);
    }
    /**
     * 更新midstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    default void writeMidstreamResponse(RetryableContext context, U response, int state) {
        writeResponse(context, null, response, null, state);
    }

    /**
     * 更新downstream状态.
     * @param context object
     * @param response response
     * @param state state
     * @return
     */
    default void writeDownstreamResponse(RetryableContext context, S response, int state) {
        writeResponse(context, null, null, response, state);
    }

    /**
     * 读取上游返回的对象.
     * @param context object
     * @param cls class
     * @return
     */
    T readUpstreamResponse(RetryableContext context, Class<T> cls);
    /**
     * 读取本地处理返回的对象.
     * @param context object
     * @param cls class
     * @return
     */
    U readMidstreamResponse(RetryableContext context, Class<U> cls);
    /**
     * 读取下游处理返回的对象.
     * @param context object
     * @param cls class
     * @return
     */
    S readDownstreamResponse(RetryableContext context, Class<S> cls);

    /**
     * 读取上游返回的对象.
     * @param context object
     * @return
     */
    T readUpstreamResponse(RetryableContext context);
    /**
     * 读取本地处理返回的对象.
     * @param context object
     * @return
     */
    U readMidstreamResponse(RetryableContext context);
    /**
     * 读取下游处理返回的对象.
     * @param context object
     * @return
     */
    S readDownstreamResponse(RetryableContext context);

}
