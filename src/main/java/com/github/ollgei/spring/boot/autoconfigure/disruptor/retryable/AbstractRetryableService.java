package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorPublisher;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorService;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.spring.SpringOllgeiDisruptorSubscription;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步重试.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractRetryableService<C extends RetryableContext, T extends RetryableUpstreamResponse, U extends RetryableMidstreamResponse, S extends RetryableDownstreamResponse>
        implements RetryableService<C, T, U, S>, OllgeiDisruptorService<C> {

    private OllgeiDisruptorPublisher publisher;

    @Override
    public void publish(C context) {
        Assert.notNull(publisher, "OllgeiDisruptorPublisher not null!");
        final SpringOllgeiDisruptorSubscription subscription = new SpringOllgeiDisruptorSubscription();
        subscription.setContext(context);
        subscription.setClazz(this.getClass());
        publisher.write(subscription);
    }

    @Override
    public void read(C context) {
        final RetryableStateEnum rState = readState(context);
        int state = rState.getCode();
        final T uResponse;

        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.UPSTREAM_FAIL)) {
            //1 执行upstream 保持幂等性
            uResponse = upstream(context);
            if (uResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
                writeUpstreamResponse(context, uResponse, state);
            } else if (uResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.UPSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.UPSTREAM_FAIL.getCode());
                return;
            }
        } else {
            uResponse = readUpstreamResponse(context);
        }

        //2 执行中游业务处理 保持幂等性(本地处理)
        final U mResponse;

        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.MIDSTREAM_FAIL)) {
            mResponse = midstream(context, uResponse);
            if (mResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
                writeMidstreamResponse(context, mResponse, state);
            } else if (mResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.MIDSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.MIDSTREAM_FAIL.getCode());
                return;
            }
        } else {
            mResponse = readMidstreamResponse(context);
        }

        //判断第3位是0
        final S dResponse;
        if (RetryableStateEnum.hasFail(state, RetryableStateEnum.DOWNSTREAM_FAIL)) {
            //3 执行下游业务处理 保持幂等性
            dResponse = downstream(context, uResponse, mResponse);
            if (dResponse.getResult() == RetryableResultEnum.SUCCESS) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
                writeDownstreamResponse(context, dResponse, state);
                return;
            } else if (dResponse.getResult() == RetryableResultEnum.NOOP) {
                state = state | RetryableStateEnum.DOWNSTREAM_SUCCESS.getCode();
            } else {
                writeState(context, state & RetryableStateEnum.DOWNSTREAM_FAIL.getCode());
                return;
            }
        }
        //最后写入state
        writeState(context, state, true);
    }

    public OllgeiDisruptorPublisher getPublisher() {
        return publisher;
    }

    @Autowired
    public void setPublisher(OllgeiDisruptorPublisher publisher) {
        this.publisher = publisher;
    }
}
