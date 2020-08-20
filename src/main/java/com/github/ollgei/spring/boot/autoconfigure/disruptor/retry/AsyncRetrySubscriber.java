package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class AsyncRetrySubscriber implements OllgeiDisruptorSubscriber<AsyncRetrySubscription> {

    private AsyncRetryService asyncRetryService;

    public AsyncRetrySubscriber(AsyncRetryService asyncRetryService) {
        this.asyncRetryService = asyncRetryService;
    }

    @Override
    public void onNext(AsyncRetrySubscription subscription) {
        final AsyncRetryObject object = new AsyncRetryObject();
        object.setRequest(subscription.getRequest());
        object.setContext(subscription.getContext());
        asyncRetryService.run(object);
    }
}