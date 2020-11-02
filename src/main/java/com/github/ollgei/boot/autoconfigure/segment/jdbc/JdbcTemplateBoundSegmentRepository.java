package com.github.ollgei.boot.autoconfigure.segment.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.ollgei.boot.autoconfigure.internal.jdbc.AbstractJdbcTemplateRepository;
import com.github.ollgei.boot.autoconfigure.segment.BoundSegmentProperties;
import com.github.ollgei.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.boot.autoconfigure.segment.core.SectionDefinition;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateBoundSegmentRepository extends AbstractJdbcTemplateRepository implements BoundSegmentRepository {
    private final BoundSegmentProperties boundSegmentProperties;

    public JdbcTemplateBoundSegmentRepository(BoundSegmentProperties properties, @NonNull JdbcTemplate jdbcTemplate) {
        this(properties, jdbcTemplate, null);
    }

    public JdbcTemplateBoundSegmentRepository(BoundSegmentProperties properties, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        super(properties.getTableName(), jdbcTemplate, transactionManager);
        this.boundSegmentProperties = properties;
    }

    @Override
    public List<SectionDefinition> list() {
        final String sql = listAllocatorsSql();
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final SectionDefinition alloc = new SectionDefinition();
            alloc.setName(rs.getString(1));
            alloc.setMax(rs.getLong(2));
            alloc.setStep(rs.getInt(3));
            return alloc;
        });
    }

    @Override
    public SectionDefinition updateMaxAndStep(SectionDefinition entity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        params.put("step", new Integer(entity.getStep()));

        return updateAndGetAllocator(getUpdateMaxIdByCustomStepSql(), params);
    }

    private SectionDefinition updateAndGetAllocator(String sql, Map<String, Object> params) {

        final SectionDefinition newAllocator = transactionTemplate.execute(status -> {

            final boolean result = jdbcTemplate.update(sql, params) > 0;
            if (!result) {
                return null;
            }

            return jdbcTemplate.queryForObject(getAllocatorSql(),
                    params, (rs, rowNum) -> {
                        final SectionDefinition alloc = new SectionDefinition();
                        alloc.setName(rs.getString(1));
                        alloc.setMax(rs.getLong(2));
                        alloc.setStep(rs.getInt(3));
                        return alloc;
                    });
        });

        if (Objects.nonNull(newAllocator)) {
            return newAllocator;
        }

        log.warn("updateMaxIdByCustomStep failed");

        return null;
    }

    private String listAllocatorsSql() {
         return "SELECT name, max_id, step, update_time FROM " + sqlStatementsSource.tableName();
    }

    private String getUpdateMaxIdByCustomStepSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET max_id = max_id + :step WHERE name = :name";
    }

    private String getAllocatorSql() {
        return "SELECT name, max_id, step, update_time FROM " + sqlStatementsSource.tableName() + " WHERE name = :name";
    }
}
