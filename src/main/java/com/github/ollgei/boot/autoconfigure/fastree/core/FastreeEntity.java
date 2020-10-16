package com.github.ollgei.boot.autoconfigure.fastree.core;

import java.util.Map;

import lombok.Data;

/**
 * entity 按照 left no 排序.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class FastreeEntity implements Comparable<FastreeEntity> {

    /**
     * ID.
     */
    private Integer id;
    /**
     * gpname.
     */
    private String gpname;
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

    @Override
    public int compareTo(FastreeEntity entity) {
        return this.getLftNo() - entity.getLftNo();
    }
}
