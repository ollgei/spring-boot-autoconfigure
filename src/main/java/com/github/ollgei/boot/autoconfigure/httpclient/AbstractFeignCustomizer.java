package com.github.ollgei.boot.autoconfigure.httpclient;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public abstract class AbstractFeignCustomizer implements FeignCustomizer {

    @Override
    public int orderd() {
        return 0;
    }
}
