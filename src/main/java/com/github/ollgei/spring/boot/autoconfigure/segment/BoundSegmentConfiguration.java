package com.github.ollgei.spring.boot.autoconfigure.segment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.ollgei.spring.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentBuffer;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberBoundSegmentWatch;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.NumberElementReloadEventListener;

import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public abstract class BoundSegmentConfiguration {

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
    public NumberElementReloadEventListener numberElementReloadEventListener(NumberBoundSegmentBuffer numberBoundSegmentBuffer) {
        return new NumberElementReloadEventListener(numberBoundSegmentBuffer);
    }

}
