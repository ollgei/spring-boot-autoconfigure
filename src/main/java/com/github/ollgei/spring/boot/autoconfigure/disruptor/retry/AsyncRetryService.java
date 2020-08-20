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
        final AsyncRetryUpstreamResult upstreamResult;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            upstreamResult = localService.upstream(object);
            if (upstreamResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateUpstreamState(upstreamResult, AsyncRetryStateEnum.UPSTREAM_SUCCESS.getCode());
            } else if (upstreamResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateUpstreamState(upstreamResult, AsyncRetryStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            upstreamResult = retryRepository.readUpstreamResult(object);
        }

        //2 执行本地业务处理 保持幂等性
        final AsyncRetryLocalResult localResult;
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.LOCAL_FAIL)) {
            localResult = localService.invoke(object, upstreamResult);
            if (localResult.getValue() == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateLocalState(localResult, AsyncRetryStateEnum.LOCAL_SUCCESS.getCode());
            } else if (localResult.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateLocalState(localResult, AsyncRetryStateEnum.LOCAL_FAIL.getCode());
                return;
            }
        } else {
            localResult = retryRepository.readLocalResult(object);
        }

        //判断第3位是0
        if (AsyncRetryStateEnum.hasFail(state, AsyncRetryStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            final AsyncRetryResult result = localService.downstream(object, upstreamResult, localResult);
            if (result.getValue() == AsyncRetryResultEnum.SUCCESS) {
                retryRepository.updateDownstreamState(AsyncRetryStateEnum.DOWNSTREAM_SUCCESS.getCode());
            } else if (result.getValue() == AsyncRetryResultEnum.NOOP) {
                //continue;
            } else {
                retryRepository.updateDownstreamState(AsyncRetryStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
    }

}
