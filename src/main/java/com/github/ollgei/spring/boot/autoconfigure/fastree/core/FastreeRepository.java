package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public interface FastreeRepository {

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

}
