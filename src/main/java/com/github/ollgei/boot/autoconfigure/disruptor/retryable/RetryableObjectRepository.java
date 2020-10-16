package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableObjectRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void save(RetryableContext context, RetryableObjectModel model);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void update(RetryableContext context, RetryableObjectModel model);

    /**
     * read.
     * @param context context
     * @return model
     */
    RetryableObjectModel readModel(RetryableContext context);
}
