package com.github.ollgei.spring.boot.autoconfigure.segment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.ollgei.spring.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentBuffer;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentWatch;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberElementReloadEventListener;
import com.github.ollgei.spring.boot.autoconfigure.segment.jdbc.JdbcTemplateBoundSegmentRepository;

import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "ollgei.segment", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(BoundSegmentProperties.class)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class BoundSegmentAutoConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean
    public BoundSegmentRepository jdbcTemplateBoundSegmentRepository(BoundSegmentProperties boundSegmentProperties, JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateBoundSegmentRepository(boundSegmentProperties, jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public NumberBoundSegmentBuffer numberBoundSegmentBuffer(BoundSegmentProperties boundSegmentProperties, BoundSegmentRepository boundSegmentRepository, @Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new NumberBoundSegmentBuffer(boundSegmentProperties, boundSegmentRepository, threadPoolTaskExecutor);
    }

    @Bean
    @ConditionalOnMissingBean
    public NumberBoundSegmentWatch numberBoundSegmentWatch(BoundSegmentProperties boundSegmentProperties, BoundSegmentRepository boundSegmentRepository) {
        return new NumberBoundSegmentWatch(boundSegmentProperties, boundSegmentRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public NumberElementReloadEventListener reloadTagEventListener(NumberBoundSegmentBuffer numberBoundSegmentBuffer) {
        return new NumberElementReloadEventListener(numberBoundSegmentBuffer);
    }

}
