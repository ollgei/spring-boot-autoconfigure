package com.github.ollgei.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.hessian2.Hessian2Serialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class HessianSerializationManager implements SerializationManager {
    private Serialization serialization;

    public HessianSerializationManager() {
        this.serialization = new Hessian2Serialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
