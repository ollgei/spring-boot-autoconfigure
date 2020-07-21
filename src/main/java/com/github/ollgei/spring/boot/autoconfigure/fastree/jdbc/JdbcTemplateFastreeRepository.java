package com.github.ollgei.spring.boot.autoconfigure.fastree.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.ollgei.spring.boot.autoconfigure.fastree.FastreeProperties;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeEntity;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeRepository;
import com.github.ollgei.spring.boot.autoconfigure.jdbc.AbstractJdbcTemplateRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@Slf4j
public class JdbcTemplateFastreeRepository extends AbstractJdbcTemplateRepository implements FastreeRepository {

    private FastreeProperties fastreeProperties;

    public JdbcTemplateFastreeRepository(FastreeProperties properties, @NonNull JdbcTemplate jdbcTemplate) {
        this(properties, jdbcTemplate, null);
    }

    public JdbcTemplateFastreeRepository(FastreeProperties properties, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        super(properties.getTableName(), jdbcTemplate, transactionManager);
        this.fastreeProperties = properties;
    }

    @Override
    public Boolean save(Integer pid, FastreeEntity newEntity) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("id", pid);
            final Integer rgtId = jdbcTemplate.queryForObject(getQueryRgtIdSql(), params, (rs, rowNum) -> rs.getInt(1));
            //创建失败
            if (Objects.isNull(rgtId)) {
                return false;
            }
            //update
            final Map<String, Object> params1 = new HashMap<>();
            params.put("rgtid", rgtId);
            boolean result = jdbcTemplate.update(getUpdateRgtIdSql(), params1) > 0;
            if (!result) {
                throw new RuntimeException("更新失败");
            }
            result = jdbcTemplate.update(getUpdateLftIdSql(), params1) > 0;
            if (!result) {
                throw new RuntimeException("更新失败");
            }
            //insert
            final Map<String, Object> params3 = new HashMap<>();
            params3.put("name", newEntity.getName());
            params3.put("lftid", newEntity.getLftId());
            params3.put("rgtid", newEntity.getRgtId());
            int insertedRows = jdbcTemplate.update(getInsertSql(), params3);
            if (insertedRows != 1) {
                throw new RuntimeException("插入失败");
            }

            return true;
        });
    }

    @Override
    public FastreeEntity init(String name) {
        return transactionTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            //insert
            final Map<String, Object> params3 = new HashMap<>();
            params3.put("name", name);
            params3.put("lftid", 1);
            params3.put("rgtid", 2);
            int insertedRows = jdbcTemplate.update(getInsertSql(), new MapSqlParameterSource(params3), keyHolder);
            if (insertedRows != 1) {
                throw new RuntimeException("插入失败");
            }
            final FastreeEntity root = new FastreeEntity();
            root.setId(keyHolder.getKey().intValue());
            root.setName(name);
            root.setLftId(1);
            root.setLftId(2);
            return root;
        });
    }

    private String getQueryRgtIdSql() {
        return "SELECT rgt FROM " + sqlStatementsSource.tableName() +" WHERE id = :id FOR UPDATE";
    }

    private String getUpdateRgtIdSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgt = rgtid + 2 WHERE rgtid >= :rgtid";
    }

    private String getUpdateLftIdSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lft = lftid + 2 WHERE lftid >= :rgtid";
    }

    private String getInsertSql() {
        return "INSERT INTO " + sqlStatementsSource.tableName() + "(`name`, lftid, rgtid) VALUES (:id, :lftid, :rgtid)";
    }
}
