package com.github.ollgei.spring.boot.autoconfigure.serialization;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ollgei.base.commonj.dubbo.io.UnsafeByteArrayInputStream;
import com.github.ollgei.base.commonj.dubbo.io.UnsafeByteArrayOutputStream;
import com.github.ollgei.base.commonj.dubbo.serialize.ObjectInput;
import com.github.ollgei.base.commonj.dubbo.serialize.ObjectOutput;
import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;
import com.github.ollgei.base.commonj.errors.ErrorCodeEnum;
import com.github.ollgei.base.commonj.errors.ErrorException;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface SerializationManager {
    Logger log = LoggerFactory.getLogger(SerializationManager.class);
    /**
     * desc.
     * @return success
     */
    Serialization get();

    /**
     * 序列化对象.
     * @param object object
     * @return success
     */
    default byte[] serializeObject(SerializationObject object) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
            serialize.writeInt(object.getVersion());
            serializeNativeObject(serialize, object.getObject());
            serialize.flushBuffer();
            return os.toByteArray();
        } catch (Exception ex) {
            log.warn("序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 反序列化对象.
     * @param bytes bytes
     * @param cls class
     * @return success
     */
    default  <T> T deserializeObject(byte[] bytes, Class<T> cls) {
        final UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(bytes);
        try {
            final ObjectInput deserialize = get().deserialize(is);
            final int version = deserialize.readInt();
            if (log.isInfoEnabled()) {
                log.info("反序列化版本:{}", version);
            }
            return deserializeNativeObject(deserialize, cls);
        } catch (Exception ex) {
            log.warn("反序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 序列化对象.
     * @param object object
     * @return success
     */
    default byte[] serializeNativeObject(Object object) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
            serializeNativeObject(serialize, object);
            serialize.flushBuffer();
            return os.toByteArray();
        } catch (Exception ex) {
            log.warn("序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 序列化对象.
     * @param object object
     */
    default void serializeNativeObject(ObjectOutput serialize, Object object) throws IOException {
        if (object instanceof String) {
            serialize.writeUTF((String) object);
        } else if (object instanceof Integer) {
            serialize.writeInt(((Integer) object).intValue());
        } else if (object instanceof Long) {
            serialize.writeLong(((Long) object).longValue());
        } else if (object instanceof Double) {
            serialize.writeDouble(((Double) object).doubleValue());
        } else if (object instanceof Float) {
            serialize.writeFloat(((Float) object).floatValue());
        } else if (object instanceof Boolean) {
            serialize.writeBool(((Boolean) object).booleanValue());
        } else if (object instanceof Byte) {
            serialize.writeByte(((Byte) object).byteValue());
        } else {
            serialize.writeObject(object);
        }
    }

    /**
     * 序列化对象.
     * @param bytes bytes
     * @return success
     */
    default byte[] serializeBytes(byte[] bytes) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
            serialize.writeBytes(bytes);
            serialize.flushBuffer();
            return os.toByteArray();
        } catch (Exception ex) {
            log.warn("序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 反序列化对象.
     * @param bytes bytes
     * @return success
     */
    default byte[] deserializeBytes(byte[] bytes) {
        final UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(bytes);
        try {
            final ObjectInput deserialize = get().deserialize(is);
            return deserialize.readBytes();
        } catch (Exception ex) {
            log.warn("反序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 序列化对象.
     * @param bytes bytes
     * @return success
     */
    default byte[] serializeBytes(byte[] bytes, int off, int len) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
            serialize.writeBytes(bytes, off, len);
            serialize.flushBuffer();
            return os.toByteArray();
        } catch (Exception ex) {
            log.warn("序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 反序列化对象.
     * @param bytes bytes
     * @param cls class
     * @return success
     */
    default <T> T deserializeNativeObject(byte[] bytes, Class<T> cls) {
        final UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(bytes);
        try {
            final ObjectInput deserialize = get().deserialize(is);
            return deserializeNativeObject(deserialize, cls);
        } catch (Exception ex) {
            log.warn("反序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 反序列化对象.
     * @param deserialize deserialize
     * @param cls class
     * @return success
     */
    default <T> T deserializeNativeObject(ObjectInput deserialize , Class<T> cls) throws IOException, ClassNotFoundException {
        if (cls == String.class) {
            return (T) deserialize.readUTF();
        } else if (cls == Integer.class) {
            return (T) new Integer(deserialize.readInt());
        } else if (cls == Long.class) {
            return (T) new Long(deserialize.readLong());
        } else if (cls == Double.class) {
            return (T) new Double(deserialize.readDouble());
        } else if (cls == Float.class) {
            return (T) new Float(deserialize.readFloat());
        } else if (cls == Boolean.class) {
            return (T) new Boolean(deserialize.readBool());
        } else if (cls == Byte.class) {
            return (T) new Byte(deserialize.readByte());
        }
        return deserialize.readObject(cls);
    }
}
