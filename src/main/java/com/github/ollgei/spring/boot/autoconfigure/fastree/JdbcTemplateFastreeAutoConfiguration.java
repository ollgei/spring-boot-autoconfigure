package com.github.ollgei.spring.boot.autoconfigure.fastree;

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

import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeRepository;
import com.github.ollgei.spring.boot.autoconfigure.fastree.jdbc.JdbcTemplateFastreeRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentConfiguration;

import static com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties.PREFIX;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(FastreeProperties.class)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcTemplateFastreeAutoConfiguration extends BoundSegmentConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean
    public FastreeRepository fastreeRepository(FastreeProperties fastreeProperties, JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateFastreeRepository(fastreeProperties, jdbcTemplate);
    }

}
