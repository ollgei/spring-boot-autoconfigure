package com.github.ollgei.spring.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.kryo.optimized.KryoSerialization2;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class Kryo2SerializationManager implements SerializationManager {
    private Serialization serialization;

    public Kryo2SerializationManager() {
        this.serialization = new KryoSerialization2();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
