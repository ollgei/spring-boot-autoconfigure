/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ollgei.boot.autoconfigure.gson.cache;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.github.ollgei.base.commonj.gson.Gson;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Gson.
 *
 * @author David Liu
 * @author Ivan Golovko
 * @since 1.2.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Gson.class)
@ConditionalOnMissingBean(value = GsonCacheManager.class, name = "gsonCacheResolver")
@EnableConfigurationProperties(GsonCacheProperties.class)
@Import({ GsonCacheAutoConfiguration.GsonCacheConfigurationImportSelector.class})
public class GsonCacheAutoConfiguration {

    /**
     * {@link ImportSelector} to add {@link GsonCacheType} configuration classes.
     */
    static class GsonCacheConfigurationImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            GsonCacheType[] types = GsonCacheType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = GsonCacheConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }

    }

}
