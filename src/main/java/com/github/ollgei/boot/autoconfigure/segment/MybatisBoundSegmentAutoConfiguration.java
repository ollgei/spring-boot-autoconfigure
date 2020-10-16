package com.github.ollgei.boot.autoconfigure.segment;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.apache.ibatis.session.SqlSessionFactory;

import static com.github.ollgei.boot.autoconfigure.segment.BoundSegmentProperties.PREFIX;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = PREFIX + ".mybatis", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(BoundSegmentProperties.class)
@ConditionalOnClass({ SqlSessionFactory.class, DataSource.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisBoundSegmentAutoConfiguration extends BoundSegmentConfiguration {

}
