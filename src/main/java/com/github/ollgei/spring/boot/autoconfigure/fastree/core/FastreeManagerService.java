package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * manager.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeManagerService {

    /**
     * init code可以是中文.
     * @param code code
     * @return success
     */
    default FastreeEntity init(String code) {
        return init(code, code);
    }

    /**
     * init code可以是中文.
     * @param code code
     * @return success
     */
    default FastreeEntity init(String code, Map<String, Object> custom) {
        return init(code, code, custom);
    }

    /**
     * init.
     * @param code code
     * @param gpname group name
     * @return success
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
     * @param gpname group name
     * @param custom custom
     * @return success
     */
    default FastreeEntity init(String gpname, String code, Map<String, Object> custom) {
        final FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setCode(code);
        keyEntity.setGpname(gpname);
        return init(keyEntity, custom);
    }

    /**
     * init.
     * @param keyEntity keyEntity
     * @param custom custom
     * @return success
     */
    FastreeEntity init(FastreeKeyEntity keyEntity, Map<String, Object> custom);

    /**
     * query none lock.
     * @param id id
     * @return success
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
     * @return success
     */
    Integer queryLevel(Integer id);

    /**
     * query none lock.
     * @param keyEntity keyEntity
     * @return success
     */
    Integer queryLevel(FastreeKeyEntity keyEntity);

    /**
     * 增加.
     * @param pcode pcode
     * @param code code
     * @return success
     */
    default FastreeEntity addOne(String pcode, String code) {
        FastreeKeyEntity keyEntity = new FastreeKeyEntity();
        keyEntity.setGpname(pcode);
        keyEntity.setCode(code);
        return addOne(keyEntity, code, Collections.emptyMap());
    }

    /**
     * 增加.
     * @param pid parent id
     * @param code code
     * @return success
     */
    default FastreeEntity addOne(Integer pid, String code) {
        return addOne(pid, code, Collections.emptyMap());
    }

    /**
     * 增加.
     * @param keyEntity keyEntity
     * @param code code
     * @param custom custom
     * @return success
     */
    FastreeEntity addOne(FastreeKeyEntity keyEntity, String code, Map<String, Object> custom);

    /**
     * 增加.
     * @param pid parent id
     * @param code code
     * @return success
     */
    FastreeEntity addOne(Integer pid, String code, Map<String, Object> custom);

    /**
     * remove.
     * @param keyEntity keyEntity
     */
    void removeIncludeChildren(FastreeKeyEntity keyEntity);

    /**
     * 增加.
     * @param id id
     */
    void removeIncludeChildren(Integer id);

    /**
     * name.
     * @return success
     */
    String name();

}
