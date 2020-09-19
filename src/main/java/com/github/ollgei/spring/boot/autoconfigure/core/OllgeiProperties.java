package com.github.ollgei.spring.boot.autoconfigure.core;

import java.util.List;

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
    /**app key*/
    private String appKey;
    /**client IDs: id:secret*/
    private List<Client> clients;

    @Data
    public static class Client {
        /**ID*/
        private String id;
        /**secret*/
        private String secret;
    }

}
