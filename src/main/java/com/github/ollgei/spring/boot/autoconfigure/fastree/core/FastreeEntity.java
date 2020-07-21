package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

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
     * name.
     */
    private String name;
    /**
     * left no.
     */
    private Integer lftNo;
    /**
     * right no.
     */
    private Integer rgtNo;

}
