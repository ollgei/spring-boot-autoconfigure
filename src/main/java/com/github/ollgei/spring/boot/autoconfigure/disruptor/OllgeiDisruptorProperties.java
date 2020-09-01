/*
 * Copyright 2017-2019 Lemonframework Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.ollgei.spring.boot.autoconfigure.disruptor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * OllgeiDisruptorProperties.
 *
 * @author jiawei
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = OllgeiDisruptorProperties.DISRUPTOR_PREFIX)
@Data
public class OllgeiDisruptorProperties {

    public static final String DISRUPTOR_PREFIX = "ollgei.disruptor";

    private int bufferSize = 4096;

    private int subscriberSize = 16;

    private boolean multi = true;

    private boolean globalQueue = true;

    private String subscriberName = "ollgei";

    private Retryable retryable = new Retryable();

    @Data
    public static final class Retryable {
        /**
         * delay(seconds).
         */
        private Integer delay = 10;

        /**
         * multiplier.
         */
        private Double multiplier = 1.0d;

        /**
         * maxAttempts.
         */
        private Integer maxAttempts = 5;

        /**
         * batchSize.
         */
        private Integer batchSize = 80;
    }

}