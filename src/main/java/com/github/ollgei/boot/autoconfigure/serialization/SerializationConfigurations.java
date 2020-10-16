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

package com.github.ollgei.boot.autoconfigure.serialization;

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
final class SerializationConfigurations {

	private static final Map<SerializationType, Class<?>> MAPPINGS;

	static {
		Map<SerializationType, Class<?>> mappings = new EnumMap<>(SerializationType.class);
        mappings.put(SerializationType.KRYO2, Kryo2SerializationConfiguration.class);
        mappings.put(SerializationType.GSON, GsonSerializationConfiguration.class);
        mappings.put(SerializationType.KRYO, KryoSerializationConfiguration.class);
        mappings.put(SerializationType.JAVA, JavaSerializationConfiguration.class);
        mappings.put(SerializationType.COMPACTED_JAVA, CompactedjavaSerializationConfiguration.class);
        mappings.put(SerializationType.PROTOSTUFF, ProtostuffSerializationConfiguration.class);
        mappings.put(SerializationType.FST, FstSerializationConfiguration.class);
        mappings.put(SerializationType.NATIVE_HESSIAN, NativeHessianSerializationConfiguration.class);
        mappings.put(SerializationType.HESSIAN, HessianSerializationConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}

	private SerializationConfigurations() {
	}

	static String getConfigurationClass(SerializationType serializationType) {
		Class<?> configurationClass = MAPPINGS.get(serializationType);
		Assert.state(configurationClass != null, () -> "Unknown cache type " + serializationType);
		return configurationClass.getName();
	}

	static SerializationType getType(String configurationClassName) {
		for (Map.Entry<SerializationType, Class<?>> entry : MAPPINGS.entrySet()) {
			if (entry.getValue().getName().equals(configurationClassName)) {
				return entry.getKey();
			}
		}
		throw new IllegalStateException("Unknown configuration class " + configurationClassName);
	}

}
