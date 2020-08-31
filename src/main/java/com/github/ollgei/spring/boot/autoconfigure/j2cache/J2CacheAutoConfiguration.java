package com.github.ollgei.spring.boot.autoconfigure.j2cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.github.ollgei.base.commonj.j2cache.CacheChannel;
import com.github.ollgei.base.commonj.j2cache.J2Cache;
import com.github.ollgei.base.commonj.j2cache.J2CacheBuilder;
import com.github.ollgei.base.commonj.j2cache.J2CacheConfig;

/**
 * 启动入口 TODO: 待优化.
 *
 * @author zhangsaizz
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(J2Cache.class)
@EnableConfigurationProperties({J2CacheProperties.class})
@PropertySource(value = {"${ollgei.j2cache.config-location}", "file:${ollgei.j2cache.config-location}"}, ignoreResourceNotFound = true)
public class J2CacheAutoConfiguration {

    @Autowired
    private StandardEnvironment standardEnvironment;

    @Bean
    public J2CacheConfig j2CacheConfig() {
        return SpringJ2CacheConfigUtil.initFromConfig(standardEnvironment);
    }

    @Bean
    @DependsOn("j2CacheConfig")
    public CacheChannel cacheChannel(J2CacheConfig j2CacheConfig) {
        J2CacheBuilder builder = J2CacheBuilder.init(j2CacheConfig);
        return builder.getChannel();
    }

}
