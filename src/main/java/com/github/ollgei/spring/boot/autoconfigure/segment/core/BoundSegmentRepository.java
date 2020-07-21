package com.github.ollgei.spring.boot.autoconfigure.segment.core;

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
    List<SectionDefination> list();

    /**
     * updateMaxAndStep.
     * @param entity defination
     * @return update and get
     */
    SectionDefination updateMaxAndStep(SectionDefination entity);

}
