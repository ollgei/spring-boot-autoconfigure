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
     * name.
     */
    private String name;
    /**
     * left id.
     */
    private Integer lftId;
    /**
     * right id.
     */
    private Integer rgtId;

}
