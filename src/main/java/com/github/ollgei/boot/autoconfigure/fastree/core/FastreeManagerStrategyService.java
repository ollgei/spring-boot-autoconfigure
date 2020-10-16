package com.github.ollgei.boot.autoconfigure.fastree.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * entity.
 *
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class FastreeManagerStrategyService {

    private Map<String, FastreeManagerService> services = new ConcurrentHashMap<>(8);

    public FastreeManagerStrategyService(List<FastreeManagerService> managers) {
        for (FastreeManagerService service: managers) {
            services.put(service.name(), service);
        }
    }

    public FastreeManagerService get(String name) {
        if (!services.containsKey(name)) {
            log.warn("Not found name {}", name);
            throw new RuntimeException("Unsupported Strategy");
        }
        return services.get(name);
    }

    public FastreeManagerService common() {
        return services.get(CommonFastreeManagerService.NAME);
    }
}
