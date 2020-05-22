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

package com.github.ollgei.spring.boot.autoconfigure.disruptor.core;

import com.lmax.disruptor.EventTranslatorOneArg;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步事件转换器.
 *
 * @author jiawei
 * @since 1.0.0
 */
@Slf4j
class InternalEventTranslator implements EventTranslatorOneArg<InternalEvent, AbstractSubcription> {

    @Override
    public void translateTo(InternalEvent event, long sequence, AbstractSubcription subcription) {
        subcription.setSequence(sequence);
        event.setSubcription(subcription);
        if (log.isInfoEnabled()) {
            log.info("producer->Thread:{}, sequence:{}, size: {}, data:{}",
                    Thread.currentThread().getName(),
                    sequence,
                    event.size(),
                    subcription);
        }
    }
}
