package com.github.ollgei.boot.autoconfigure.segment.core;

import java.util.List;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface BoundSegmentRepository {

    /**
     * list.
     * @return list
     */
    List<SectionDefinition> list();

    /**
     * updateMaxAndStep.
     * @param entity defination
     * @return update and get
     */
    SectionDefinition updateMaxAndStep(SectionDefinition entity);

}
