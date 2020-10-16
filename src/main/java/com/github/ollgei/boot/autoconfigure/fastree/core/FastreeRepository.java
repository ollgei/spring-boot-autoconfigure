package com.github.ollgei.boot.autoconfigure.fastree.core;

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
     * @param key key
     */
    List<FastreeEntity> queryWithChildren(FastreeKeyEntity key);

    /**
     * query none lock.
     * @param key key
     */
    List<FastreeEntity> queryWithParent(FastreeKeyEntity key);

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
     * @param key key
     */
    FastreeEntity queryParent(FastreeKeyEntity key);

    /**
     * query none lock.
     * @param id id
     */
    Integer queryLevel(Integer id);

    /**
     * query none lock.
     * @param key key
     */
    Integer queryLevel(FastreeKeyEntity key);

    /**
     * save.
     * @param pcode parent code
     * @param code code
     */
    default FastreeEntity save(String pcode, String code) {
        FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setGpname(pcode);
        keyEntity.setCode(pcode);
        return save(keyEntity, code, Collections.emptyMap());
    }

    /**
     * save.
     * @param pid parent id
     * @param code code
     */
    default FastreeEntity save(Integer pid, String code) {
        return save(pid, code, Collections.emptyMap());
    }

    /**
     * save.
     * @param key parent key
     * @param code code
     */
    FastreeEntity save(FastreeKeyEntity key, String code, Map<String, Object> custom);

    /**
     * save.
     * @param pid parent id
     * @param code code
     */
    FastreeEntity save(Integer pid, String code, Map<String, Object> custom);

    /**
     * init.
     * @param code code
     * @param gpname gpname
     */
    default FastreeEntity init(String gpname, String code) {
        FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setCode(code);
        keyEntity.setGpname(gpname);
        return init(keyEntity, Collections.emptyMap());
    }

    /**
     * init.
     * @param key key
     */
    FastreeEntity init(FastreeKeyEntity key, Map<String, Object> custom);

    /**
     * remove.
     * @param id id
     */
    Boolean remove(Integer id);

    /**
     * remove.
     * @param keyEntity keyEntity
     */
    Boolean remove(FastreeKeyEntity keyEntity);

}
