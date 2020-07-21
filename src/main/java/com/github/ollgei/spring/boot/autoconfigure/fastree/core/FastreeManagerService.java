package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

/**
 * entity.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeManagerService {

    /**
     * 增加.
     * @param pid parent id
     * @param entity entity
     * @return
     */
    void addOne(Integer pid, FastreeEntity entity);

    /**
     * init.
     * @param name name
     * @return
     */
    FastreeEntity init(String name);

}
