package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import lombok.Data;

@Data
public class RetryableResponseModel<T> {

    private T response;

    private RetryableResultEnum result;

    public RetryableResponseModel(T response, RetryableResultEnum result) {
        this.response = response;
        this.result = result;
    }

    public RetryableResponseModel(RetryableResultEnum result) {
        this.result = result;
    }

}
