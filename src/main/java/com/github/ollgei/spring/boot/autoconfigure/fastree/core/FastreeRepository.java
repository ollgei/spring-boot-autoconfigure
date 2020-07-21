package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.List;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeRepository {

    /**
     * query none lock.
     * @param id id
     */
    List<FastreeEntity> queryWithChildren(Integer id);

    /**
     * query none lock.
     * @param name name
     */
    List<FastreeEntity> queryWithChildren(String name);

    /**
     * save.
     * @param pname parent name
     * @param name name
     */
    Boolean save(String pname, String name);

    /**
     * save.
     * @param pid parent id
     * @param name name
     */
    Boolean save(Integer pid, String name);

    /**
     * init.
     * @param name name
     * @param kind kind
     */
    FastreeEntity init(String kind, String name);

    /**
     * remove.
     * @param id id
     */
    Boolean remove(Integer id);

    /**
     * remove.
     * @param name name
     */
    Boolean remove(String name);

}
