package com.github.ollgei.spring.boot.autoconfigure.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@ConfigurationProperties("ollgei")
@Data
public class OllgeiProperties {

    private String appId;

}
