package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.List;

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
     * @param name name
     */
    List<FastreeEntity> queryWithChildren(String name);

    /**
     * 增加.
     * @param pname pname
     * @param name name
     * @return
     */
    void addOne(String pname,String name);

    /**
     * 增加.
     * @param pid parent id
     * @param name name
     * @return
     */
    void addOne(Integer pid, String name);

    /**
     * init.
     * @param name name
     * @param kind kind
     * @return
     */
    FastreeEntity init(String kind, String name);


    /**
     * init.
     * @param name name
     * @return
     */
    default FastreeEntity init(String name) {
        return init(name, name);
    }

    /**
     * remove.
     * @param name name
     * @return
     */
    void removeIncludeChildren(String name);

    /**
     * 增加.
     * @param id id
     * @return
     */
    void removeIncludeChildren(Integer id);

}
