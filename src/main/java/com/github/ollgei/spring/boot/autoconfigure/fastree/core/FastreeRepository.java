package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    FastreeEntity queryParent(Integer id);

    /**
     * query none lock.
     * @param code code
     */
    FastreeEntity queryParent(String code);

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
    default Boolean save(String pcode, String code) {
        return save(pcode, code, Collections.emptyMap());
    }

    /**
     * save.
     * @param pid parent id
     * @param code code
     */
    default Boolean save(Integer pid, String code) {
        return save(pid, code, Collections.emptyMap());
    }

    /**
     * save.
     * @param pcode parent code
     * @param code code
     */
    Boolean save(String pcode, String code, Map<String, Object> custom);

    /**
     * save.
     * @param pid parent id
     * @param code code
     */
    Boolean save(Integer pid, String code, Map<String, Object> custom);

    /**
     * init.
     * @param code code
     * @param gpname gpname
     */
    default FastreeEntity init(String gpname, String code) {
        return init(gpname, code, Collections.emptyMap());
    }

    /**
     * init.
     * @param code code
     * @param gpname gpname
     */
    FastreeEntity init(String gpname, String code, Map<String, Object> custom);

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
