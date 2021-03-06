package com.github.ollgei.boot.autoconfigure.serialization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import org.nustaq.serialization.FSTObjectInput;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ FSTObjectInput.class })
@ConditionalOnMissingBean(SerializationManager.class)
@Conditional(SerializationCondition.class)
class FstSerializationConfiguration {

    @Bean
    @SuppressWarnings("all")
    FstSerializationManager fstSerializationManager() {
        return new FstSerializationManager();
    }

}
