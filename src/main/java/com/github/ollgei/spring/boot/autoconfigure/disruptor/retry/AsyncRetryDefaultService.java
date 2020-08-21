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
public abstract class AsyncRetryDefaultService implements AsyncRetryService {

    private OllgeiDisruptorPublisher publisher;

    private AsyncRetryLocalService localService;

    public AsyncRetryDefaultService(OllgeiDisruptorPublisher publisher, AsyncRetryLocalService retryLocalService) {
        this.publisher = publisher;
        this.localService = retryLocalService;
    }

    @Override
    public void init(Object context) {
        //序列化到数据库中，方便重试使用
        localService.persist(context);
        //异步发布消息
        final AsyncRetrySubscription subscription = new AsyncRetrySubscription();
        subscription.setContext(context);
        subscription.setKind(kind());
        publisher.write(subscription);
    }

    @Override
    public void run(Object context) {
        final AsyncRetryStateEnum rState = localService.readState(context);
        final int state = (rState == AsyncRetryStateEnum.INIT) ? AsyncRetryStateEnum.UPSTREAM_FAIL.getCode() : rState.getCode();
        final AsyncRetryUpstreamResponse uResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            final AsyncRetryResult<? extends AsyncRetryUpstreamResponse> uResult = localService.upstream(context);
            uResponse = uResult.getResponse();
            if (uResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                localService.writeUpstreamResponse(context, uResponse);
            } else if (uResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.writeState(context, AsyncRetryStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = localService.readUpstreamResponse(context);
        }

        //2 执行本地业务处理 保持幂等性
        final AsyncRetryLocalResponse lResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.LOCAL_FAIL)) {
            final AsyncRetryResult<? extends AsyncRetryLocalResponse> lResult = localService.invoke(context, uResponse);
            lResponse = lResult.getResponse();
            if (lResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                localService.writeLocalResponse(context, lResponse);
            } else if (lResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.writeState(context, AsyncRetryStateEnum.LOCAL_FAIL.getCode());
                return;
            }
        } else {
            lResponse = localService.readLocalResponse(context);
        }

        //判断第3位是0
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final AsyncRetryResultEnum result = localService.downstream(context, uResponse, lResponse);
            if (result == AsyncRetryResultEnum.SUCCESS) {
                localService.writeState(context, AsyncRetryStateEnum.DOWNSTREAM_SUCCESS.getCode());
            } else if (result == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                localService.writeState(context, AsyncRetryStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
    }

}
