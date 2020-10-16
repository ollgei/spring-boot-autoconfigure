package com.github.ollgei.boot.autoconfigure.serialization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(SerializationManager.class)
@Conditional(SerializationCondition.class)
class JavaSerializationConfiguration {

    @Bean
    @SuppressWarnings("all")
    JavaSerializationManager javaSerializationManager() {
        return new JavaSerializationManager();
    }

}
