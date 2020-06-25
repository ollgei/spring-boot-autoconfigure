package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.List;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
public interface BoundSegmentRepository {

    /**
     * list.
     */
    List<SectionDefination> list();

    /**
     * updateMaxAndStep.
     * @param entity defination
     * @return
     */
    SectionDefination updateMaxAndStep(SectionDefination entity);

}