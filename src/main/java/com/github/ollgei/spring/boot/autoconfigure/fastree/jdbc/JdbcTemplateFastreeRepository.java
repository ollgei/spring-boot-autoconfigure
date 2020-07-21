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
    public Boolean save(String pname, String name) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("name", pname);
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySqlUseName(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setKind(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }
            return saveWithTransaction(parentEntity.getKind(), parentEntity.getRgtNo(), name);
        });
    }

    @Override
    public Boolean save(Integer pid, String name) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("id", pid);
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySql(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setKind(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }

            return saveWithTransaction(parentEntity.getKind(), parentEntity.getRgtNo(), name);
        });
    }

    private Boolean saveWithTransaction(String kind, Integer rgtNo, String name) {
        //update
        final Map<String, Object> params1 = new HashMap<>();
        params1.put("rgtno", rgtNo);
        params1.put("kind", kind);
        boolean result = jdbcTemplate.update(getUpdateRgtNoSql(), params1) > 0;
        if (!result) {
            throw new RuntimeException("更新失败");
        }
        result = jdbcTemplate.update(getUpdateLftNoSql(), params1) > 0;
        if (!result) {
            log.warn("no found for lftno >= {}", rgtNo);
        }
        //insert
        final Map<String, Object> params2 = new HashMap<>();
        params2.put("kind", kind);
        params2.put("name", name);
        params2.put("lftno", rgtNo);
        params2.put("rgtno", rgtNo + 1);
        int insertedRows = jdbcTemplate.update(getInsertSql(), params2);
        if (insertedRows != 1) {
            throw new RuntimeException("插入失败");
        }

        return true;
    }

    @Override
    public FastreeEntity init(String kind, String name) {
        return transactionTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            //insert
            final Map<String, Object> params3 = new HashMap<>();
            params3.put("kind", kind);
            params3.put("name", name);
            params3.put("lftno", 1);
            params3.put("rgtno", 2);
            int insertedRows = jdbcTemplate.update(getInsertSql(), new MapSqlParameterSource(params3), keyHolder);
            if (insertedRows != 1) {
                throw new RuntimeException("插入失败");
            }
            final FastreeEntity root = new FastreeEntity();
            root.setId(keyHolder.getKey().intValue());
            root.setName(name);
            root.setLftNo(1);
            root.setLftNo(2);
            return root;
        });
    }

    private String getQuerySql() {
        return "SELECT kind, rgtno FROM " + sqlStatementsSource.tableName() +" WHERE id = :id FOR UPDATE";
    }

    private String getQuerySqlUseName() {
        return "SELECT kind, rgtno FROM " + sqlStatementsSource.tableName() +" WHERE name = :name FOR UPDATE";
    }

    private String getUpdateRgtNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgtno = rgtno + 2 WHERE kind = :kind AND rgtno >= :rgtno";
    }

    private String getUpdateLftNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lftno = lftno + 2 WHERE kind = :kind AND lftno >= :rgtno";
    }

    private String getInsertSql() {
        return "INSERT INTO " + sqlStatementsSource.tableName() + "(kind, `name`, lftno, rgtno) VALUES (:kind, :name, :lftno, :rgtno)";
    }
}
