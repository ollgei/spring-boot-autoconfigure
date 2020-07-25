package com.github.ollgei.spring.boot.autoconfigure.fastree;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.github.ollgei.spring.boot.autoconfigure.fastree.core.CommonFastreeManagerService;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeManagerService;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeManagerStrategyService;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeRepository;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public abstract class FastreeConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CommonFastreeManagerService commonFastreeManagerService(FastreeProperties fastreeProperties, FastreeRepository fastreeRepository) {
        return new CommonFastreeManagerService(fastreeProperties, fastreeRepository);
    }

    @Bean
    public FastreeManagerStrategyService fastreeManagerStrategyService(ObjectProvider<FastreeManagerService> fastreeManagerServices) {
        return new FastreeManagerStrategyService(
                fastreeManagerServices.stream().collect(Collectors.toList()));
    }

}
