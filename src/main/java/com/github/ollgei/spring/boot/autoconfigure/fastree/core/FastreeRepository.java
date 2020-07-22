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
     * @param code code
     */
    List<FastreeEntity> queryWithChildren(String code);

    /**
     * query none lock.
     * @param code code
     */
    List<FastreeEntity> queryWithParent(String code);

    /**
     * query none lock.
     * @param id id
     */
    List<FastreeEntity> queryWithParent(Integer id);

    /**
     * query none lock.
     * @param id id
     */
    Integer queryLevel(Integer id);

    /**
     * query none lock.
     * @param code code
     */
    Integer queryLevel(String code);

    /**
     * save.
     * @param pcode parent code
     * @param code code
     */
    Boolean save(String pcode, String code);

    /**
     * save.
     * @param pid parent id
     * @param code code
     */
    Boolean save(Integer pid, String code);

    /**
     * init.
     * @param code code
     * @param kind kind
     */
    FastreeEntity init(String kind, String code);

    /**
     * remove.
     * @param id id
     */
    Boolean remove(Integer id);

    /**
     * remove.
     * @param code code
     */
    Boolean remove(String code);

}
