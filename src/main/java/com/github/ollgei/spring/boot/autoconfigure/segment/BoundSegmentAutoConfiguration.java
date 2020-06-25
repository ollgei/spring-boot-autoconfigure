package com.github.ollgei.spring.boot.autoconfigure.segment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.ollgei.spring.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentWatch;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NullBoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentBuffer;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberElementReloadEventListener;

import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = "ollgei.segment", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(BoundSegmentProperties.class)
public class BoundSegmentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BoundSegmentRepository InmemoryBoundSegmentRepository() {
        return new NullBoundSegmentRepository();
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
