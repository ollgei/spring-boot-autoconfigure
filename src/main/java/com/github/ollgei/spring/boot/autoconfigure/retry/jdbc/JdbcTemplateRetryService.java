package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.support.RetrySynchronizationManager;

import com.github.ollgei.spring.boot.autoconfigure.retry.RetryService;

/**
 * service.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryService implements RetryService {

    public void execute() {
        JdbcTemplateRetryContext context = new JdbcTemplateRetryContext(RetrySynchronizationManager.getContext());
        context.setCnt(1);
        RetrySynchronizationManager.register(context);
        try {
            //执行内部方法
        } finally {
            RetrySynchronizationManager.clear();
        }
    }


}
