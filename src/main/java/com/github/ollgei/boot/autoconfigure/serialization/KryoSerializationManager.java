package com.github.ollgei.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.kryo.KryoSerialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class KryoSerializationManager implements SerializationManager {
    private Serialization serialization;

    public KryoSerializationManager() {
        this.serialization = new KryoSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
