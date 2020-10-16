package com.github.ollgei.boot.autoconfigure.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = OllgeiRedisProperties.PREFIX)
public class OllgeiRedisProperties {

    public static final String PREFIX = "ollgei.redis";

}

