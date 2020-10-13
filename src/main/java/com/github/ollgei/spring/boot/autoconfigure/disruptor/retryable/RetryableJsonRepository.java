package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableJsonRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void save(RetryableContext context, RetryableJsonModel model);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void update(RetryableContext context, RetryableJsonModel model);

    /**
     * read.
     * @param context context
     * @return model
     */
    RetryableJsonModel readModel(RetryableContext context);
}
