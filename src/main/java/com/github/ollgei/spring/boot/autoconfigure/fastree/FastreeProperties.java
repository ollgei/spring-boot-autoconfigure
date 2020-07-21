package com.github.ollgei.spring.boot.autoconfigure.fastree;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = FastreeProperties.PREFIX)
public class FastreeProperties {
    public static final String PREFIX = "ollgei.fastree";

    private String tableName = "tb_fastree";

    private String columns = "";
}
