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
    public List<FastreeEntity> queryWithChildren(String code) {
        return repository.queryWithChildren(code);
    }

    @Override
    public List<FastreeEntity> queryWithParent(String code) {
        return repository.queryWithParent(code);
    }

    @Override
    public List<FastreeEntity> queryWithParent(Integer id) {
        return repository.queryWithParent(id);
    }

    @Override
    public FastreeEntity queryParent(String code) {
        return repository.queryParent(code);
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
    public Integer queryLevel(String code) {
        return repository.queryLevel(code);
    }

    @Override
    public void addOne(Integer pid, String code, Map<String, Object> custom) {
        repository.save(pid, code, custom);
    }

    @Override
    public void addOne(String pcode, String code, Map<String, Object> custom) {
        repository.save(pcode, code, custom);
    }

    @Override
    public FastreeEntity init(String kind, String code, Map<String, Object> custom) {
        return repository.init(kind, code, custom);
    }

    @Override
    public void removeIncludeChildren(String code) {
        repository.remove(code);
    }

    @Override
    public void removeIncludeChildren(Integer id) {
        repository.remove(id);
    }
}
