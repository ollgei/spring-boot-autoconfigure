package com.github.ollgei.spring.boot.autoconfigure.fastree;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.github.ollgei.spring.boot.autoconfigure.fastree.core.DefaultFastreeManagerService;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeManagerService;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public abstract class FastreeConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FastreeManagerService fastreeManagerService(FastreeProperties fastreeProperties, FastreeRepository fastreeRepository) {
        return new DefaultFastreeManagerService(fastreeRepository);
    }

}
