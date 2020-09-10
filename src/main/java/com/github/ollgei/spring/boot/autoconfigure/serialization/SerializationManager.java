package com.github.ollgei.spring.boot.autoconfigure.serialization;

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
     * @return
     */
    Serialization get();

    /**
     * 序列化对象.
     * @param object object
     * @return
     */
    default byte[] serializeObject(SerializationObject object) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
            serialize.writeInt(object.getVersion());
            serialize.writeObject(object.getObject());
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
     * @return
     */
    default  <T> T deserializeObject(byte[] bytes, Class<T> cls) {
        final UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(bytes);
        try {
            final ObjectInput deserialize = get().deserialize(is);
            final int version = deserialize.readInt();
            if (log.isInfoEnabled()) {
                log.info("反序列化版本:{}", version);
            }
            return deserialize.readObject(cls);
        } catch (Exception ex) {
            log.warn("反序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 序列化对象.
     * @param object object
     * @return
     */
    default byte[] serializeNativeObject(Object object) {
        final UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream();
        try {
            final ObjectOutput serialize = get().serialize(os);
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
            serialize.flushBuffer();
            return os.toByteArray();
        } catch (Exception ex) {
            log.warn("序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }

    /**
     * 序列化对象.
     * @param bytes bytes
     * @return
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
     * @return
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
     * @return
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
     * @return
     */
    default <T> T deserializeNativeObject(byte[] bytes, Class<T> cls) {
        final UnsafeByteArrayInputStream is = new UnsafeByteArrayInputStream(bytes);
        try {
            final ObjectInput deserialize = get().deserialize(is);
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
        } catch (Exception ex) {
            log.warn("反序列化失败:{}", ex.getMessage());
            throw new ErrorException(ErrorCodeEnum.FAIL_SERIALIZATION, ex);
        }
    }
}
