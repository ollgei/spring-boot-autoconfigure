package com.github.ollgei.spring.boot.autoconfigure.disruptor.retry;

import com.github.ollgei.base.commonj.errors.CauseException;
import com.github.ollgei.base.commonj.errors.ErrorCodeEnum;
import com.github.ollgei.base.commonj.errors.ErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * state.
 * @author zjw
 * @since 1.1.0
 */
@Getter
@AllArgsConstructor
public enum AsyncRetryStateEnum {
    /**初始 000*/
    INIT(0),
    /**本地成功 001*/
    LOCAL_SUCCESS(1),
    /**本地失败 110*/
    LOCAL_FAIL(~1 & 0x07),
    /**上游成功 010*/
    UPSTREAM_SUCCESS(2),
    /**上游失败 101*/
    UPSTREAM_FAIL(~2 & 0x07),
    /**下游成功 100*/
    DOWNSTREAM_SUCCESS(4),
    /**下游失败 011*/
    DOWNSTREAM_FAIL(~4 & 0x07);

    private int code;

    public static boolean hasSuccess(int state, AsyncRetryStateEnum successState) {
        return (state & successState.getCode()) == successState.getCode();
    }

    public static boolean hasFail(int state, AsyncRetryStateEnum failState) {
        return (state | failState.getCode()) == failState.getCode();
    }

    public static AsyncRetryStateEnum resolve(int state) {
        final AsyncRetryStateEnum[] values = AsyncRetryStateEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getCode() == state) {
                return values[i];
            }
        }
        throw new ErrorException(ErrorCodeEnum.ILLEGAL_STATE, new CauseException("异步重试状态不正确"));
    }
}