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

package com.github.ollgei.boot.autoconfigure.gson;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Gson.
 *
 * @author David Liu
 * @author Ivan Golovko
 * @since 1.2.0
 */
@ConditionalOnProperty(prefix = OllgeiGsonProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Gson.class)
@EnableConfigurationProperties(OllgeiGsonProperties.class)
public class OllgeiGsonAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GsonBuilder ollgeiGsonBuilder(List<OllgeiGsonBuilderCustomizer> customizers) {
		GsonBuilder builder = new GsonBuilder();
		customizers.forEach((c) -> c.customize(builder));
		return builder;
	}

	@Bean
    @ConditionalOnClass(name = "springfox.documentation.spring.web.json.Json")
    public OllgeiGsonBuilderCustomizer springfoxGsonBuilderCustomizer() {
	    return new SpringfoxGsonBuilderCustomizer();
    }

	@Bean
	@ConditionalOnMissingBean
	public Gson ollgeiGson(GsonBuilder gsonBuilder) {
		return gsonBuilder.create();
	}

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnClass(name = "org.springframework.http.converter.HttpMessageConverter")
//    public OllgeiGsonHttpMessageConverter ollgeiGsonHttpMessageConverter(Gson gson) {
//        final OllgeiGsonHttpMessageConverter converter = new OllgeiGsonHttpMessageConverter();
//        converter.setGson(gson);
//        return converter;
//    }

	@Bean
	public StandardGsonBuilderCustomizer ollgeiStandardGsonBuilderCustomizer(OllgeiGsonProperties gsonProperties) {
		return new StandardGsonBuilderCustomizer(gsonProperties);
	}

	static final class StandardGsonBuilderCustomizer implements OllgeiGsonBuilderCustomizer, Ordered {

		private final OllgeiGsonProperties properties;

		StandardGsonBuilderCustomizer(OllgeiGsonProperties properties) {
			this.properties = properties;
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public void customize(GsonBuilder builder) {
			OllgeiGsonProperties properties = this.properties;
			PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
			map.from(properties::getGenerateNonExecutableJson).toCall(builder::generateNonExecutableJson);
			map.from(properties::getExcludeFieldsWithoutExposeAnnotation)
					.toCall(builder::excludeFieldsWithoutExposeAnnotation);
			map.from(properties::getSerializeNulls).whenTrue().toCall(builder::serializeNulls);
			map.from(properties::getEnableComplexMapKeySerialization).toCall(builder::enableComplexMapKeySerialization);
			map.from(properties::getDisableInnerClassSerialization).toCall(builder::disableInnerClassSerialization);
			map.from(properties::getLongSerializationPolicy).to(builder::setLongSerializationPolicy);
			map.from(properties::getFieldNamingPolicy).to(builder::setFieldNamingPolicy);
			map.from(properties::getPrettyPrinting).toCall(builder::setPrettyPrinting);
			map.from(properties::getLenient).toCall(builder::setLenient);
			map.from(properties::getDisableHtmlEscaping).toCall(builder::disableHtmlEscaping);
			map.from(properties::getDateFormat).to(builder::setDateFormat);
		}

	}

}
