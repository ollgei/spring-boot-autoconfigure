package com.github.ollgei.boot.autoconfigure.segment;

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

import com.github.ollgei.boot.autoconfigure.segment.jdbc.JdbcTemplateBoundSegmentRepository;
import com.github.ollgei.boot.autoconfigure.segment.core.BoundSegmentRepository;

import static com.github.ollgei.boot.autoconfigure.segment.BoundSegmentProperties.PREFIX;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(BoundSegmentProperties.class)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcTemplateBoundSegmentAutoConfiguration extends BoundSegmentConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean
    public BoundSegmentRepository jdbcTemplateBoundSegmentRepository(BoundSegmentProperties boundSegmentProperties, JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateBoundSegmentRepository(boundSegmentProperties, jdbcTemplate);
    }

}
