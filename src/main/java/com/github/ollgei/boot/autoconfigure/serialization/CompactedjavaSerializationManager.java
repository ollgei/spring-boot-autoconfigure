package com.github.ollgei.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.java.CompactedJavaSerialization;

public class CompactedjavaSerializationManager implements SerializationManager {
    private Serialization serialization;

    public CompactedjavaSerializationManager() {
        this.serialization = new CompactedJavaSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
