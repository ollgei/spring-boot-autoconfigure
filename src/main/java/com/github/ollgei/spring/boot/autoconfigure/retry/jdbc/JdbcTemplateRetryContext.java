package com.github.ollgei.spring.boot.autoconfigure.retry.jdbc;

import java.util.List;

import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
public class JdbcTemplateRetryContext extends RetryContextSupport {
    private int cnt;
    private List<String> keys;
    private List<RetryRecord> records;

    public JdbcTemplateRetryContext(RetryContext parent, List<String> keys) {
        super(parent);
        this.keys = keys;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<RetryRecord> getRecords() {
        return records;
    }

    public void setRecords(List<RetryRecord> records) {
        this.records = records;
    }

    @Override
    public int getRetryCount() {
        return cnt;
    }
}