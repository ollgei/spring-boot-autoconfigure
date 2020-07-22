package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.Map;

import lombok.Data;

/**
 * entity.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class FastreeEntity {

    /**
     * ID.
     */
    private Integer id;
    /**
     * kind.
     */
    private String kind;
    /**
     * code.
     */
    private String code;
    /**
     * left no.
     */
    private Integer lftNo;
    /**
     * right no.
     */
    private Integer rgtNo;

    /**
     * 自定义的字段.
     */
    private Map<String, Object> custom;

}
