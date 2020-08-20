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

    private AsyncRetryRepository retryRepository;

    private AsyncRetryLocalService localService;

    public AsyncRetryService(OllgeiDisruptorPublisher publisher, AsyncRetryRepository retryRepository, AsyncRetryLocalService retryLocalService) {
        this.publisher = publisher;
        this.retryRepository = retryRepository;
        this.localService = retryLocalService;
    }

    public void start(AsyncRetryObject object) {
        //写入到数据库中
        retryRepository.persist(object);
        //异步发布消息
        final AsyncRetrySubscription subscription = new AsyncRetrySubscription();
        subscription.setContext(object.getContext());
        subscription.setRequest(object.getRequest());
        publisher.write(subscription);
    }

    public void run(AsyncRetryObject object) {
        final AsyncRetryStateEnum rState = retryRepository.readState(object);
        final int state = (rState == AsyncRetryStateEnum.INIT) ? AsyncRetryStateEnum.UPSTREAM_FAIL.getCode() : rState.getCode();
        final AsyncRetryUpstreamResponse uResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            final AsyncRetryResult<AsyncRetryUpstreamResponse> uResult = localService.upstream(object);
            uResponse = uResult.getReponse();
            if (uResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateUpstreamState(uResponse, AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode());
            } else if (uResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateUpstreamState(uResponse, AsyncRetryStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = retryRepository.readUpstreamResponse(object);
        }

        //2 执行本地业务处理 保持幂等性
        final AsyncRetryLocalResponse lResponse;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.LOCAL_FAIL)) {
            final AsyncRetryResult<AsyncRetryLocalResponse> lResult = localService.invoke(object, uResponse);
            lResponse = lResult.getReponse();
            if (lResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateLocalState(lResult.getReponse(), AsyncRetryStateEnum.LOCAL_SUCCESS.getCode());
            } else if (lResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateLocalState(lResult.getReponse(), AsyncRetryStateEnum.LOCAL_FAIL.getCode());
                return;
            }
        } else {
            lResponse = retryRepository.readLocalResponse(object);
        }

        //判断第3位是0
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final AsyncRetryResultEnum result = localService.downstream(object, uResponse, lResponse);
            if (result == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateDownstreamState(AsyncRetryStateEnum.DOWNSTREAM_SUCCESS.getCode());
            } else if (result == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateDownstreamState(AsyncRetryStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
    }

}
