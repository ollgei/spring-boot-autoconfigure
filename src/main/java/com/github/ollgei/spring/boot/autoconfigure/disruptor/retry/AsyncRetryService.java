package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class AsyncRetryService {

    private OllgeiDisruptorPublisher publisher;

    private AsyncRetryLocalService localService;

    public AsyncRetryService(OllgeiDisruptorPublisher publisher, AsyncRetryLocalService retryLocalService) {
        this.publisher = publisher;
        this.localService = retryLocalService;
    }

    public void start(AsyncRetryObject object) {
        //写入到数据库中
        localService.persist(object);
        //异步发布消息
        final AsyncRetrySubscription subscription = new AsyncRetrySubscription();
        subscription.setContext(object.getContext());
        subscription.setKind(object.getKind());
        publisher.write(subscription);
    }

    public void run(AsyncRetryObject object) {
        final AsyncRetryStateEnum rState = localService.readState(object);
        final int state = (rState == AsyncRetryStateEnum.INIT) ? AsyncRetryStateEnum.UPSTREAM_FAIL.getCode() : rState.getCode();
        final AsyncRetryUpstreamResponse uResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            final AsyncRetryResult<AsyncRetryUpstreamResponse> uResult = localService.upstream(object);
            uResponse = uResult.getReponse();
            if (uResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                localService.writeUpstreamResponse(uResponse);
            } else if (uResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.updateState(AsyncRetryStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = localService.readUpstreamResponse(object);
        }

        //2 执行本地业务处理 保持幂等性
        final AsyncRetryLocalResponse lResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.LOCAL_FAIL)) {
            final AsyncRetryResult<AsyncRetryLocalResponse> lResult = localService.invoke(object, uResponse);
            lResponse = lResult.getReponse();
            if (lResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                localService.writeLocalResponse(lResult.getReponse());
            } else if (lResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.updateState(AsyncRetryStateEnum.LOCAL_FAIL.getCode());
                return;
            }
        } else {
            lResponse = localService.readLocalResponse(object);
        }

        //判断第3位是0
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final AsyncRetryResultEnum result = localService.downstream(object, uResponse, lResponse);
            if (result == AsyncRetryResultEnum.SUCCESS) {
                localService.updateState(AsyncRetryStateEnum.DOWNSTREAM_SUCCESS.getCode());
            } else if (result == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.updateState(AsyncRetryStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
    }

}
