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
}
