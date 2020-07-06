package com.github.ollgei.spring.boot.autoconfigure.retry;

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

import com.github.ollgei.spring.boot.autoconfigure.retry.jdbc.JdbcTemplateRetryOperationsInterceptor;
import com.github.ollgei.spring.boot.autoconfigure.retry.jdbc.JdbcTemplateRetryRepository;

import static com.github.ollgei.spring.boot.autoconfigure.retry.RetryProperties.PREFIX;

/**
 * auto configuration.
 * @author ollgei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = PREFIX + ".jdbctemplate", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(RetryProperties.class)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class JdbcTemplateRetryAutoConfiguration {

    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean
    public JdbcTemplateRetryRepository jdbcTemplateRetryRepository(RetryProperties retryProperties, JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateRetryRepository(retryProperties, jdbcTemplate);
    }

    @Bean("jdbcTemplateInterceptor")
    @ConditionalOnMissingBean
    private JdbcTemplateRetryOperationsInterceptor jdbcTemaplteInterceptor(JdbcTemplateRetryRepository jdbcTemplateRetryRepository) {
        return new JdbcTemplateRetryOperationsInterceptor(jdbcTemplateRetryRepository);
    }

}
