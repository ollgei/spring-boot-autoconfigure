package com.github.ollgei.spring.boot.autoconfigure.segment;

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

import com.github.ollgei.spring.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.jdbc.JdbcTemplateBoundSegmentRepository;

import static com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties.SEGMENT_PREFIX;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = SEGMENT_PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true", matchIfMissing = true)
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
