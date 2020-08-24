package com.github.ollgei.spring.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.gson.GsonSerialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class GsonSerializationManager implements SerializationManager {
    @Override
    public Serialization get() {
        return new GsonSerialization();
    }
}
