package com.github.ollgei.spring.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.gson.GsonSerialization;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class GsonSerializationManager implements SerializationManager {

    private Serialization serialization;

    public GsonSerializationManager() {
        this.serialization = new GsonSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
