package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface AsyncRetryRepository {

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
     * @param result result
     * @param state 新状态
     * @return
     */
    <T extends AsyncRetryUpstreamResult> int updateUpstreamState(T result, int state);
    /**
     * 更新local状态.
     * @param result result
     * @param state 新状态
     * @return
     */
    <T extends AsyncRetryLocalResult> int updateLocalState(T result, int state);
    /**
     * 更新状态.
     * @param state 新状态
     * @return
     */
    int updateDownstreamState(int state);
    /**
     * 读取上游返回的对象.
     * @param object object
     * @return
     */
    <T extends AsyncRetryUpstreamResult> T readUpstreamResult(AsyncRetryObject object);
    /**
     * 读取本地处理返回的对象.
     * @param object object
     * @return
     */
    <T extends AsyncRetryLocalResult> T readLocalResult(AsyncRetryObject object);

}
