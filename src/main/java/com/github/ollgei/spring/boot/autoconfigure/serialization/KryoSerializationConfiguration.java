package com.github.ollgei.spring.boot.autoconfigure.serialization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.esotericsoftware.kryo.Kryo;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ Kryo.class })
@ConditionalOnMissingBean(SerializationManager.class)
@Conditional(SerializationCondition.class)
class KryoSerializationConfiguration {

    @Bean
    @SuppressWarnings("all")
    KryoSerializationManager kryoSerializationManager() {
        return new KryoSerializationManager();
    }

}
