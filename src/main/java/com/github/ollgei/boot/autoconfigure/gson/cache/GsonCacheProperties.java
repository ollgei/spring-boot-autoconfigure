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

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.ollgei.base.commonj.gson.Gson;
import lombok.Data;

/**
 * Configuration properties to configure {@link Gson}.
 *
 * @author Ivan Golovko
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "ollgei.gson.cache")
@Data
public class GsonCacheProperties {
    /**
     * Cache type. By default, auto-detected according to the environment.
     */
    private GsonCacheType type;

    /**
     * expire.
     */
    private int expire = 600;

    /**
     * expire.
     */
    private int maxSize = 10000;

}
