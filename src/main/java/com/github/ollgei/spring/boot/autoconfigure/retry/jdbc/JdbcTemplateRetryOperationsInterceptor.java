package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import org.springframework.retry.interceptor.NewMethodArgumentsIdentifier;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateRetryOperationsInterceptor extends StatefulRetryOperationsInterceptor {

    private JdbcTemplateRetryRepository jdbcTemplateRetryRepository;

    public JdbcTemplateRetryOperationsInterceptor(JdbcTemplateRetryRepository retryRepository) {
        this.jdbcTemplateRetryRepository = retryRepository;
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new JdbcTemplateRetryPolicy(retryRepository));
//        retryTemplate.setBackOffPolicy(new JdbcTemplateBackOffPolicy(retryRepository));
        setRetryOperations(retryTemplate);
//        setNewItemIdentifier(new JdbcTemplateNewMethodArgumentsIdentifier());
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Object[] args = invocation.getArguments();
        if (args[0] instanceof JdbcTemplateRetryContext) {
            JdbcTemplateRetryContext context = (JdbcTemplateRetryContext) args[0];
            RetrySynchronizationManager.register(context);
            try {
                return super.invoke(invocation);
            } finally {
                RetrySynchronizationManager.clear();
            }
        } else {
            return super.invoke(invocation);
        }
    }

    private static class JdbcTemplateNewMethodArgumentsIdentifier implements NewMethodArgumentsIdentifier {

        @Override
        public boolean isNew(Object[] args) {
            return true;
        }
    }
}
