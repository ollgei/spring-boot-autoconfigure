package com.github.ollgei.boot.autoconfigure.fastree;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.github.ollgei.boot.autoconfigure.fastree.core.FastreeRepository;
import com.github.ollgei.boot.autoconfigure.fastree.jdbc.JdbcTemplateFastreeRepository;

import static com.github.ollgei.boot.autoconfigure.fastree.FastreeProperties.PREFIX;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(FastreeProperties.class)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcTemplateFastreeAutoConfiguration extends FastreeConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean
    public FastreeRepository fastreeRepository(FastreeProperties fastreeProperties, JdbcTemplate jdbcTemplate) {
        final JdbcTemplateFastreeRepository repository = new JdbcTemplateFastreeRepository(fastreeProperties.getTableName(), jdbcTemplate);
        if (StringUtils.hasText(fastreeProperties.getColumns())) {
            repository.setColumns(fastreeProperties.getColumns());
        }
        return repository;
    }

}
