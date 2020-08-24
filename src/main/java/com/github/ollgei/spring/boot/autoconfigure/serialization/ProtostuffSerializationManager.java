package com.github.ollgei.spring.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.protostuff.ProtostuffSerialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class ProtostuffSerializationManager implements SerializationManager {
    private Serialization serialization;

    public ProtostuffSerializationManager() {
        this.serialization = new ProtostuffSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
