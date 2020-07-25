package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.List;
import java.util.Map;

/**
 * desc.
 * @author 1.0.0
 * @since zjw
 */
public class DefaultFastreeManagerService implements FastreeManagerService {

    private FastreeRepository repository;

    public DefaultFastreeManagerService(FastreeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FastreeEntity> queryWithChildren(Integer id) {
        return repository.queryWithChildren(id);
    }

    @Override
    public List<FastreeEntity> queryWithChildren(FastreeKeyEntity keyEntity) {
        return repository.queryWithChildren(keyEntity);
    }

    @Override
    public List<FastreeEntity> queryWithParent(FastreeKeyEntity keyEntity) {
        return repository.queryWithParent(keyEntity);
    }

    @Override
    public List<FastreeEntity> queryWithParent(Integer id) {
        return repository.queryWithParent(id);
    }

    @Override
    public FastreeEntity queryParent(FastreeKeyEntity keyEntity) {
        return repository.queryParent(keyEntity);
    }

    @Override
    public FastreeEntity queryParent(Integer id) {
        return repository.queryParent(id);
    }

    @Override
    public Integer queryLevel(Integer id) {
        return repository.queryLevel(id);
    }

    @Override
    public Integer queryLevel(FastreeKeyEntity keyEntity) {
        return repository.queryLevel(keyEntity);
    }

    @Override
    public void addOne(Integer pid, String code, Map<String, Object> custom) {
        repository.save(pid, code, custom);
    }

    @Override
    public void addOne(FastreeKeyEntity keyEntity, String code, Map<String, Object> custom) {
        repository.save(keyEntity, code, custom);
    }

    @Override
    public FastreeEntity init(FastreeKeyEntity keyEntity, Map<String, Object> custom) {
        return repository.init(keyEntity, custom);
    }

    @Override
    public void removeIncludeChildren(FastreeKeyEntity keyEntity) {
        repository.remove(keyEntity);
    }

    @Override
    public void removeIncludeChildren(Integer id) {
        repository.remove(id);
    }
}
