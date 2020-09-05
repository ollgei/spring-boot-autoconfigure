package com.github.ollgei.spring.boot.autoconfigure.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * properties.
 * @author ollgei
 * @since 1.0.0
 */
@ConfigurationProperties("ollgei")
@Data
public class OllgeiProperties {

    /**app id*/
    private String appId;
    /**app name*/
    private String appName;

}
