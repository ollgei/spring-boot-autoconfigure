package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorContext;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;

/**
 * 异步重试.
 * @author ollgei
 * @since 1.0.0
 */
public interface AsyncRetryService<C extends OllgeiDisruptorContext, T extends AsyncRetryUpstreamResponse, U extends AsyncRetryMidstreamResponse> extends OllgeiDisruptorService<C> {
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
     * @param mResponse midstream
     * @param uResponse upstream
     * @return
     */
    AsyncRetryResultEnum downstream(C context, T uResponse, U mResponse);
    /**
     * 持久化到数据库中.
     * @param context context
     * @return
     */
    void init(C context);
    /**
     * lock.
     * @param context context
     * @return
     */
    void lockAndRun(C context);
    /**
     * unlock.
     * @param context context
     * @return
     */
    void unlock(C context);
    /**
     * 读取状态 {@link AsyncRetryStateEnum}.
     * @param context context
     * @return
     */
    AsyncRetryStateEnum readState(C context);
    /**
     * 更新状态.
     * @param context object
     * @param state 新状态
     * @return
     */
    void writeState(C context, int state);
    /**
     * 更新upstream状态.
     * @param context object
     * @param response response
     * @return
     */
    default void writeUpstreamResponse(C context, T response) {
        writeUpstreamResponse(context, response, AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode());
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
        writeMidstreamResponse(context, response, AsyncRetryStateEnum.MIDSTREAM_SUCCESS.getCode());
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
}
