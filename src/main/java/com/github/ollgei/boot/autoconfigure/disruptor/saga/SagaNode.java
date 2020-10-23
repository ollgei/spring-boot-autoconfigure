package com.github.ollgei.boot.autoconfigure.disruptor.saga;

import java.util.List;

import com.github.ollgei.base.commonj.model.UriWithMethod;
import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@Data
public class SagaNode {
    private String bizId;

    private UriWithMethod uriWithMethod;

    private SagaNode next;

    private List<SagaNode> parallels;

    private String backwardUrlSuffix = "/backward";

}
