package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.util.List;

import com.github.ollgei.base.commonj.enums.HttpMethod;
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

    private String method = HttpMethod.POST.name();

    private UrlNode next;

    private List<UrlNode> parallels;

}
