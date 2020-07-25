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
     * @param keyEntity keyEntity
     */
    List<FastreeEntity> queryWithChildren(FastreeKeyEntity keyEntity);

    /**
     * query none lock.
     * @param keyEntity keyEntity
     */
    List<FastreeEntity> queryWithParent(FastreeKeyEntity keyEntity);

    /**
     * query none lock.
     * @param id id
     */
    List<FastreeEntity> queryWithParent(Integer id);

    /**
     * query none lock.
     * @param keyEntity keyEntity
     */
    FastreeEntity queryParent(FastreeKeyEntity keyEntity);

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
     * @param keyEntity keyEntity
     */
    Integer queryLevel(FastreeKeyEntity keyEntity);

    /**
     * 增加.
     * @param pcode pcode
     * @param code code
     * @return
     */
    default void addOne(String pcode, String code) {
        FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setGpname(pcode);
        keyEntity.setCode(code);
        addOne(keyEntity, code, Collections.emptyMap());
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
     * @param keyEntity keyEntity
     * @param code code
     * @return
     */
    void addOne(FastreeKeyEntity keyEntity, String code, Map<String, Object> custom);

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
     * @param gpname gpname
     * @return
     */
    default FastreeEntity init(String gpname, String code) {
        FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setCode(code);
        keyEntity.setGpname(gpname);
        return init(keyEntity, Collections.emptyMap());
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
     * @param keyEntity keyEntity
     * @return
     */
    FastreeEntity init(FastreeKeyEntity keyEntity, Map<String, Object> custom);

    /**
     * remove.
     * @param keyEntity keyEntity
     * @return
     */
    void removeIncludeChildren(FastreeKeyEntity keyEntity);

    /**
     * 增加.
     * @param id id
     * @return
     */
    void removeIncludeChildren(Integer id);

    String name();

}
