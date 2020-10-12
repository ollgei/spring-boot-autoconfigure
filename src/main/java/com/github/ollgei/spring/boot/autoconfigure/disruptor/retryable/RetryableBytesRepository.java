package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * repository.
 * @author ollgei
 * @since 1.0
 */
public interface RetryableBytesRepository extends RetryableRepository {
    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void save(RetryableContext context, RetryableBytesModel model);

    /**
     * desc.
     * @param context context
     * @param model model
     * @return
     */
    void update(RetryableContext context, RetryableBytesModel model);

    /**
     * read.
     * @param context context
     * @return model
     */
    RetryableBytesModel readModel(RetryableContext context);

}
