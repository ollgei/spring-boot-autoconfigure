package com.github.ollgei.boot.autoconfigure.segment.core;

import lombok.Data;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Data
public class SectionDefinition {
    private String name;
    private Long max;
    private Integer step;
}
