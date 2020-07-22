package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

import java.util.List;

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
    public List<FastreeEntity> queryWithChildren(String name) {
        return repository.queryWithChildren(name);
    }

    @Override
    public List<FastreeEntity> queryWithParent(String name) {
        return repository.queryWithParent(name);
    }

    @Override
    public List<FastreeEntity> queryWithParent(Integer id) {
        return repository.queryWithParent(id);
    }

    @Override
    public Integer queryLevel(Integer id) {
        return repository.queryLevel(id);
    }

    @Override
    public Integer queryLevel(String name) {
        return repository.queryLevel(name);
    }

    @Override
    public void addOne(Integer pid, String name) {
        repository.save(pid, name);
    }

    @Override
    public void addOne(String pname, String name) {
        repository.save(pname, name);
    }

    @Override
    public FastreeEntity init(String kind, String name) {
        return repository.init(kind, name);
    }

    @Override
    public void removeIncludeChildren(String name) {
        repository.remove(name);
    }

    @Override
    public void removeIncludeChildren(Integer id) {
        repository.remove(id);
    }
}
