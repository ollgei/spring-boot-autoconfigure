package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.interceptor.NewMethodArgumentsIdentifier;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StringUtils;

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
            final List<RetryRecord> records = jdbcTemplateRetryRepository.listRecord(context);
            RetrySynchronizationManager.register(context);
            try {
                //调用
                RetryTemplate retryTemplate = new RetryTemplate();
                List<Object> objects = new ArrayList<>();
                for (RetryRecord record : records) {
                    objects.add(retryTemplate.execute(new MethodInvocationRetryCallback(invocation, record.getName())));
                }
                return objects;
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

    /**
     * @author Dave Syer
     *
     */
    private static final class MethodInvocationRetryCallback
            implements RetryCallback<Object, Throwable> {

        private final MethodInvocation invocation;
        private String label;

        private MethodInvocationRetryCallback(MethodInvocation invocation, String label) {
            this.invocation = invocation;
            if (StringUtils.hasText(label)) {
                this.label = label;
            }
            else {
                this.label = invocation.getMethod().toGenericString();
            }
        }

        @Override
        public Object doWithRetry(RetryContext context) throws Exception {
            context.setAttribute(RetryContext.NAME, label);
            try {
                return this.invocation.proceed();
            }
            catch (Exception e) {
                throw e;
            }
            catch (Error e) {
                throw e;
            }
            catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
