package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * entity.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeManagerService {

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
     * @param code code
     */
    FastreeEntity queryParent(String code);

    /**
     * query none lock.
     * @param id id
     */
    FastreeEntity queryParent(Integer id);

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
     * 增加.
     * @param pcode pcode
     * @param code code
     * @return
     */
    default void addOne(String pcode, String code) {
        addOne(pcode, code, Collections.emptyMap());
    }

    /**
     * 增加.
     * @param pid parent id
     * @param code code
     * @return
     */
    default void addOne(Integer pid, String code) {
        addOne(pid, code, Collections.emptyMap());
    }

    /**
     * 增加.
     * @param pcode pcode
     * @param code code
     * @return
     */
    void addOne(String pcode, String code, Map<String, Object> custom);

    /**
     * 增加.
     * @param pid parent id
     * @param code code
     * @return
     */
    void addOne(Integer pid, String code, Map<String, Object> custom);

    /**
     * init.
     * @param code code
     * @param kind kind
     * @return
     */
    default FastreeEntity init(String kind, String code) {
        return init(kind, code, Collections.emptyMap());
    }


    /**
     * init.
     * @param code code
     * @return
     */
    default FastreeEntity init(String code) {
        return init(code, code);
    }

    /**
     * init.
     * @param code code
     * @param kind kind
     * @return
     */
    FastreeEntity init(String kind, String code, Map<String, Object> custom);

    /**
     * remove.
     * @param code code
     * @return
     */
    void removeIncludeChildren(String code);

    /**
     * 增加.
     * @param id id
     * @return
     */
    void removeIncludeChildren(Integer id);

}
