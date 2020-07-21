package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeRepository {

    /**
     * save.
     * @param pid parent id
     */
    Boolean save(Integer pid, FastreeEntity newEntity);

    /**
     * init.
     * @param name name
     */
    FastreeEntity init(String name);

}
