package com.github.ollgei.spring.boot.autoconfigure.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class BytesRedisTemplate extends RedisTemplate<Object, Object> {

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
     * and {@link #afterPropertiesSet()} still need to be called.
     */
    public BytesRedisTemplate() {
        setKeySerializer(RedisSerializer.byteArray());
        setValueSerializer(RedisSerializer.byteArray());
        setHashKeySerializer(RedisSerializer.byteArray());
        setHashValueSerializer(RedisSerializer.byteArray());
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
     *
     * @param connectionFactory connection factory for creating new connections
     */
    public BytesRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

}
