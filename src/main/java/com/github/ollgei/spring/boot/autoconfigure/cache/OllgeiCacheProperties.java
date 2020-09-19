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

package com.github.ollgei.spring.boot.autoconfigure.cache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.ollgei.base.commonj.gson.Gson;
import lombok.Data;

/**
 * Configuration properties to configure {@link Gson}.
 *
 * @author Ivan Golovko
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = OllgeiCacheProperties.PREFIX)
@Data
public class OllgeiCacheProperties {

    public static final String PREFIX = "ollgei.cache";

    /**caches*/
    private Map<String, RedisOptions> redis = new HashMap<>();

    @Data
    public static class RedisOptions {
        /**
         * Entry expiration.
         */
        private Duration timeToLive = Duration.ofSeconds(1);
        /**
         * Allow caching null values.
         */
        private boolean cacheNullValues = true;
    }

}
