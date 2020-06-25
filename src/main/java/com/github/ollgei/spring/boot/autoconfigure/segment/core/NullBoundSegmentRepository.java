package com.github.ollgei.spring.boot.autoconfigure.segment.core;

import java.util.List;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
public class NullBoundSegmentRepository implements BoundSegmentRepository {

    @Override
    public List<SectionDefination> list() {
        throw new UnsupportedOperationException("not supported list for boundSegmentRepository");
    }


    @Override
    public SectionDefination updateMaxAndStep(SectionDefination entity) {
        throw new UnsupportedOperationException("not supported updateMaxAndStep for boundSegmentRepository");
    }

}
