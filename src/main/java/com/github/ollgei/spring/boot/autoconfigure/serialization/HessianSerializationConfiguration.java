package com.github.ollgei.spring.boot.autoconfigure.serialization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.alibaba.com.caucho.hessian.io.HessianInput;

/**
 * simple cache.
 * @author zjw
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ HessianInput.class })
@ConditionalOnMissingBean(SerializationManager.class)
@Conditional(SerializationCondition.class)
class HessianSerializationConfiguration {

    @Bean
    @SuppressWarnings("all")
    HessianSerializationManager nativeHessianManager() {
        return new HessianSerializationManager();
    }

}
