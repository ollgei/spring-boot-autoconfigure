package com.github.ollgei.boot.autoconfigure.serialization;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.dubbo.serialize.fst.FstSerialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class FstSerializationManager implements SerializationManager {
    private Serialization serialization;

    public FstSerializationManager() {
        this.serialization = new FstSerialization();
    }

    @Override
    public Serialization get() {
        return this.serialization;
    }
}
