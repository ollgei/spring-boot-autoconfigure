package com.github.ollgei.spring.boot.autoconfigure.serialization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.github.ollgei.base.commonj.dubbo.serialize.Serialization;

/**
 * autoconfiguration.
 * @author ollgei
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Serialization.class)
@ConditionalOnMissingBean(value = SerializationManager.class, name = "serializationResolver")
@EnableConfigurationProperties(SerializationProperties.class)
@Import({ SerializationAutoconfiguration.SerializationConfigurationImportSelector.class})
public class SerializationAutoconfiguration {

    /**
     * {@link ImportSelector} to add {@link SerializationType} configuration classes.
     */
    static class SerializationConfigurationImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            SerializationType[] types = SerializationType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = SerializationConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }

    }
}
