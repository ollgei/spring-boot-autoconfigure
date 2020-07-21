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
}
