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

package com.github.ollgei.spring.boot.autoconfigure.gson;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.util.Assert;

/**
 * Mappings between {@link CacheType} and {@code @Configuration}.
 *
 * @author Phillip Webb
 * @author Eddú Meléndez
 */
final class GsonCacheConfigurations {

	private static final Map<GsonCacheType, Class<?>> MAPPINGS;

	static {
		Map<GsonCacheType, Class<?>> mappings = new EnumMap<>(GsonCacheType.class);
		mappings.put(GsonCacheType.CAFFEINE, CaffeineGsonCacheConfiguration.class);
		mappings.put(GsonCacheType.SIMPLE, SimpleGsonCacheConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}

	private GsonCacheConfigurations() {
	}

	static String getConfigurationClass(GsonCacheType cacheType) {
		Class<?> configurationClass = MAPPINGS.get(cacheType);
		Assert.state(configurationClass != null, () -> "Unknown cache type " + cacheType);
		return configurationClass.getName();
	}

	static GsonCacheType getType(String configurationClassName) {
		for (Map.Entry<GsonCacheType, Class<?>> entry : MAPPINGS.entrySet()) {
			if (entry.getValue().getName().equals(configurationClassName)) {
				return entry.getKey();
			}
		}
		throw new IllegalStateException("Unknown configuration class " + configurationClassName);
	}

}
