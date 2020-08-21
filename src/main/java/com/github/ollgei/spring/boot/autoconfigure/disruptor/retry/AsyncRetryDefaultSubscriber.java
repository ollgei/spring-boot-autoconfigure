package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class AsyncRetryDefaultSubscriber implements AsyncRetrySubscriber {

    private AsyncRetryDefaultService asyncRetryDefaultService;

    public AsyncRetryDefaultSubscriber(AsyncRetryDefaultService asyncRetryDefaultService) {
        this.asyncRetryDefaultService = asyncRetryDefaultService;
    }

    @Override
    public void onNext(AsyncRetrySubscription subscription) {
        final AsyncRetryObject object = new AsyncRetryObject();
        object.setKind(subscription.getKind());
        object.setContext(subscription.getContext());
        asyncRetryDefaultService.run(object);
    }
}