package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.StringJoiner;

/**
 * 异步重试.
 * @author ollgei
 * @since 1.0.0
 */
public interface RetryableService<T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse> {
    /**
     * 上游处理.
     * @param context object
     * @return success
     */
    T upstream(RetryableContext context);
    /**
     * 中游处理.
     * @param context object
     * @param uResponse upstream
     * @return success
     */
    U midstream(RetryableContext context, T uResponse);
    /**
     * 下游处理.
     * @param context object
     * @param uResponse upstream
     * @return success
     */
    S downstream(RetryableContext context, T uResponse, U mResponse);


    /**
     * 更新upstream状态.
     * @param context object
     * @param uResponse upstream response
     * @param mResponse midstream response
     * @param dResponse downstream response
     * @param state state
     */
    void writeResponse(RetryableContext context, T uResponse, U mResponse, S dResponse, int state);

    /**
     * 更新upstream状态.
     * @param context object
     * @param response upstream response
     * @param state state
     */
    default void writeUpstreamResponse(RetryableContext context, T response, int state) {
        writeResponse(context, response, null, null, state);
    }
    /**
     * 更新midstream状态.
     * @param context object
     * @param response response
     * @param state state
     */
    default void writeMidstreamResponse(RetryableContext context, U response, int state) {
        writeResponse(context, null, response, null, state);
    }

    /**
     * 更新downstream状态.
     * @param context object
     * @param response response
     * @param state state
     */
    default void writeDownstreamResponse(RetryableContext context, S response, int state) {
        writeResponse(context, null, null, response, state);
    }

    /**
     * 返回上游返回的类.
     * @return success
     */
    Class<?> getUpstreamResponseClass();

    /**
     * 返回上游返回的类.
     * @return success
     */
    Class<?> getMidstreamResponseClass();

    /**
     * 返回上游返回的类.
     * @return success
     */
    Class<?> getDownstreamResponseClass();

    /**
     * build unique key.
     * @param context context
     * @return key
     */
    default String buildKey(RetryableContext context) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(context.getAppId());
        joiner.add(context.getKind());
        joiner.add(context.getTag());
        joiner.add(String.valueOf(context.getSeqNo()));
        return joiner.toString();
    }

    /**
     * desc.
     * @return true:binary false:string
     */
    default boolean canBinary() {
        return false;
    }

}
