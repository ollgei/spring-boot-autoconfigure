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

package com.github.ollgei.spring.boot.autoconfigure.shedlock;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Gson.
 *
 * @author David Liu
 * @author Ivan Golovko
 * @since 1.2.0
 */
@ConditionalOnProperty(prefix = OllgeiShedlockProperties.PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OllgeiShedlockProperties.class)
@ConditionalOnClass({ JdbcTemplateLockProvider.class, DataSource.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcTemplateShedlockAutoConfiguration {

    private OllgeiShedlockProperties.Jdbctemplate ollgeiShedlockProperties;

    public JdbcTemplateShedlockAutoConfiguration(OllgeiShedlockProperties ollgeiShedlockProperties) {
        this.ollgeiShedlockProperties = ollgeiShedlockProperties.getJdbctemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public LockProvider lockProvider(ObjectProvider<JdbcTemplate> jdbcTemplates) {
        final JdbcTemplateLockProvider.Configuration.Builder builder = JdbcTemplateLockProvider.Configuration.builder();
        if (StringUtils.hasText(ollgeiShedlockProperties.getTableName())) {
            builder.withTableName(ollgeiShedlockProperties.getTableName());
        }
        if (ollgeiShedlockProperties.isUseDbTime()) {
            builder.usingDbTime();
        }
        if (Objects.nonNull(ollgeiShedlockProperties.getColumns())) {
            builder.withColumnNames(ollgeiShedlockProperties.getColumns());
        }
        if (Objects.nonNull(ollgeiShedlockProperties.getTimeZone())) {
            builder.withTimeZone(ollgeiShedlockProperties.getTimeZone());
        }
        if (StringUtils.hasText(ollgeiShedlockProperties.getLockedByValue())) {
            builder.withLockedByValue(ollgeiShedlockProperties.getLockedByValue());
        }
        builder.withJdbcTemplate(jdbcTemplates.getIfUnique());
        return new JdbcTemplateLockProvider(builder.build());
    }

}
