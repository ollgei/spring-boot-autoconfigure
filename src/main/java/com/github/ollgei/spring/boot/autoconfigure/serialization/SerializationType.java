package com.github.ollgei.spring.boot.autoconfigure.serialization;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public enum SerializationType {
    /**
     * KRYO2.
     */
    KRYO2,
    /**
     * GSON.
     */
    GSON,
    /**
     * KRYO.
     */
    KRYO,
    /**
     * KRYO.
     */
    JAVA,
    /**
     * Protostuff.
     */
    PROTOSTUFF,
    /**
     * compacted java.
     */
    COMPACTED_JAVA,
    /**
     * Fst.
     */
    FST,
    /**
     * NativeHessian.
     */
    NATIVE_HESSIAN,
    /**
     * NativeHessian.
     */
    HESSIAN;
}
