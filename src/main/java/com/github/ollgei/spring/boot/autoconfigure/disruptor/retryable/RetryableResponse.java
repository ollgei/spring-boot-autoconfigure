package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class RetryableResponse {
    /**result*/
    private RetryableResultEnum result;

    public static <T extends RetryableResponse> T from(T response, RetryableResultEnum resultEnum) {
        response.setResult(resultEnum);
        return response;
    }

    public static <T extends RetryableResponse> T success(T response) {
        return from(response, RetryableResultEnum.SUCCESS);
    }

    public static <T extends RetryableResponse> T fail(T response) {
        return from(response, RetryableResultEnum.FAIL);
    }

    public static <T extends RetryableResponse> T noop(T response) {
        return from(response, RetryableResultEnum.NOOP);
    }

    public RetryableResultEnum getResult() {
        return result;
    }

    public void setResult(RetryableResultEnum result) {
        this.result = result;
    }
}
