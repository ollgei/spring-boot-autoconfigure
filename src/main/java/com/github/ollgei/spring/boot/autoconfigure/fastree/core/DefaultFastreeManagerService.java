package com.github.ollgei.spring.boot.autoconfigure.fastree.core;

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
    public void addOne(Integer pid, FastreeEntity entity) {
        repository.save(pid, entity);
    }

    @Override
    public FastreeEntity init(String name) {
        return repository.init(name);
    }
}
