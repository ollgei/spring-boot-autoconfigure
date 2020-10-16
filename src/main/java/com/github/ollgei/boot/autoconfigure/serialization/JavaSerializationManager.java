package com.github.ollgei.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.java.JavaSerialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class JavaSerializationManager implements SerializationManager {
    private Serialization serialization;

    public JavaSerializationManager() {
        this.serialization = new JavaSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
