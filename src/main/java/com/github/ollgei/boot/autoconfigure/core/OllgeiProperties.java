package com.github.ollgei.boot.autoconfigure.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Client> clients = new ArrayList<>(8);
    /**URLs*/
    private Map<String, String> urls = new HashMap<>(8);
    /**Aliyun*/
    private AliyunOss aliyunOss = new AliyunOss();
    /**Wechat MP*/
    private WechatMp wechatMp = new WechatMp();

    @Data
    public static class Client {
        /**ID*/
        private String id;
        /**secret*/
        private String secret;
    }

    @Data
    public static class AliyunOss {
        /**
         * endpoint.
         */
        private String endpoint;
        /**
         * filePath.
         */
        private String filePath;
    }

    @Data
    public static class WechatMp {

    }

}
