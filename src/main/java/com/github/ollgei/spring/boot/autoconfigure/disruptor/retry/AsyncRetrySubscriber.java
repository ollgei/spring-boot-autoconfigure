package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import java.util.List;
import java.util.Objects;

import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.AbstractSubscription;
import com.github.ollgei.spring.boot.autoconfigure.disruptor.core.OllgeiDisruptorSubscriber;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class AsyncRetrySubscriber<T extends AbstractSubscription> implements OllgeiDisruptorSubscriber<T> {

    private List<AsyncRetryService> asyncRetryServices;

    public AsyncRetrySubscriber(List<AsyncRetryService> asyncRetryServices) {
        this.asyncRetryServices = asyncRetryServices;
    }

    @Override
    public void onNext(T subscription) {
        if (subscription instanceof AsyncRetrySubscription) {
            AsyncRetrySubscription retrySubscription = (AsyncRetrySubscription) subscription;
            final AsyncRetryObject object = new AsyncRetryObject();
            object.setKind(retrySubscription.getKind());
            object.setContext(retrySubscription.getContext());
            asyncRetryServices.stream().
                    filter(s -> Objects.isNull(s.kind()) || s.kind().equals(retrySubscription.getKind())).
                    forEach(s -> s.run(object));
        }
    }
}