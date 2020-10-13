package com.github.ollgei.spring.boot.autoconfigure.disruptor.retryable;

import java.util.List;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
public class UrlNode {
    private String bizId;

    private String uri;

    private String method;

    private List<UrlNode> parallels;

}
