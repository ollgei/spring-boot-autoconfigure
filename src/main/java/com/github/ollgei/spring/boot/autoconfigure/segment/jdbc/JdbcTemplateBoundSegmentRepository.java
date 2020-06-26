package com.github.ollgei.spring.boot.autoconfigure.segment.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.ollgei.spring.boot.autoconfigure.segment.BoundSegmentProperties;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.BoundSegmentRepository;
import com.github.ollgei.spring.boot.autoconfigure.segment.core.SectionDefination;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

/**
 * desc.
 * @author zhangjiawei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateBoundSegmentRepository implements BoundSegmentRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplateBoundSegmentConfiguration configuration;
    private final SqlStatementsSource sqlStatementsSource;
    private final BoundSegmentProperties boundSegmentProperties;

    public JdbcTemplateBoundSegmentRepository(BoundSegmentProperties boundSegmentProperties, @NonNull JdbcTemplate jdbcTemplate) {
        this(boundSegmentProperties, jdbcTemplate, null);
    }

    public JdbcTemplateBoundSegmentRepository(BoundSegmentProperties boundSegmentProperties, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this(boundSegmentProperties, JdbcTemplateBoundSegmentConfiguration.builder()
                .jdbcTemplate(jdbcTemplate)
                .transactionManager(transactionManager)
                .tableName(boundSegmentProperties.getTableName())
                .build()
        );
    }

    private JdbcTemplateBoundSegmentRepository(BoundSegmentProperties boundSegmentProperties, @NonNull JdbcTemplateBoundSegmentConfiguration configuration) {
        this.configuration = requireNonNull(configuration, "configuration can not be null");
        this.boundSegmentProperties = boundSegmentProperties;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(configuration.getJdbcTemplate());
        this.sqlStatementsSource = SqlStatementsSource.create(configuration);
        PlatformTransactionManager transactionManager = configuration.getTransactionManager() != null ?
                configuration.getTransactionManager() :
                new DataSourceTransactionManager(configuration.getJdbcTemplate().getDataSource());

        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public List<SectionDefination> list() {
        final String sql = sqlStatementsSource.listAllocators();
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final SectionDefination alloc = new SectionDefination();
            alloc.setName(rs.getString(1));
            alloc.setMax(rs.getLong(2));
            alloc.setStep(rs.getInt(3));
            return alloc;
        });
    }

    @Override
    public SectionDefination updateMaxAndStep(SectionDefination entity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        params.put("step", new Integer(entity.getStep()));

        final String sql = sqlStatementsSource.getUpdateMaxIdByCustomStep();

        return updateAndGetAllocator(sql, params);
    }

    private SectionDefination updateAndGetAllocator(String sql, Map<String, Object> params) {

        final SectionDefination newAllocator = transactionTemplate.execute(status -> {
            final boolean result = jdbcTemplate.update(sql, params) > 0;
            if (!result) {
                return null;
            }

            return jdbcTemplate.queryForObject(sqlStatementsSource.getAllocator(),
                    params, (rs, rowNum) -> {
                        final SectionDefination alloc = new SectionDefination();
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
}
