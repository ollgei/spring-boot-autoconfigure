package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since
 */
@Data
public class RetryRecord {

    private String name;
    private Integer count;

}
