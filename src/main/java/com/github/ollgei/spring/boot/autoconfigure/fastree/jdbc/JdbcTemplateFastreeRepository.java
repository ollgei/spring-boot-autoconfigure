package com.github.ollgei.spring.boot.autoconfigure.fastree.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

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
    public List<FastreeEntity> queryWithChildren(Integer id) {
        return queryWithId(id, false);
    }

    @Override
    public List<FastreeEntity> queryWithChildren(String name) {
        return queryWithName(name, false);
    }

    @Override
    public List<FastreeEntity> queryWithParent(String name) {
        return queryWithName(name, true);
    }

    @Override
    public List<FastreeEntity> queryWithParent(Integer id) {
        return queryWithId(id, true);
    }

    @Override
    public Integer queryLevel(Integer id) {
        return queryLevel(id, null);
    }

    @Override
    public Integer queryLevel(String name) {
        return queryLevel(null, name);
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
            jdbcTemplate.query(getQuerySqlUseName(), params, (rs, rowNum) -> {
                FastreeEntity entity = new FastreeEntity();
                entity.setKind(rs.getString(1));
                entity.setRgtNo(rs.getInt(2));
                return entity;
            });
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

    @Override
    public Boolean remove(Integer id) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySql(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setKind(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        entity.setLftNo(rs.getInt(3));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }

            return removeWithTransaction(parentEntity.getKind(), parentEntity.getLftNo(), parentEntity.getRgtNo());
        });
    }

    @Override
    public Boolean remove(String name) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySqlUseName(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setKind(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        entity.setLftNo(rs.getInt(3));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }

            return removeWithTransaction(parentEntity.getKind(), parentEntity.getLftNo(), parentEntity.getRgtNo());
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

    private Boolean removeWithTransaction(String kind, Integer lftNo, Integer rgtNo) {
        final Map<String, Object> params1 = new HashMap<>();
        params1.put("rgtno", rgtNo);
        params1.put("lftno", lftNo);
        params1.put("kind", kind);
        //delete
        boolean result = jdbcTemplate.update(getDeleteSql(), params1) > 0;
        if (!result) {
            throw new RuntimeException("删除失败");
        }

        result = jdbcTemplate.update(getRemoveLftNoSql(), params1) > 0;
        if (!result) {
            log.warn("no found for lftno > {}", lftNo);
        }
        result = jdbcTemplate.update(getRemoveRgtNoSql(), params1) > 0;
        if (!result) {
            log.warn("no found for rgtno > {}", rgtNo);
        }

        return true;
    }

    private List<FastreeEntity> queryWithName(String name, boolean parent) {
        return queryNodes(null, name, parent);
    }

    private List<FastreeEntity> queryWithId(Integer id, boolean parent) {
        return queryNodes(id, null, parent);
    }

    private Integer queryLevel(Integer id, String name) {
        final boolean useName = StringUtils.hasText(name);
        final Map<String, Object> params = new HashMap<>();
        if (useName) {
            params.put("name", name);
        } else {
            params.put("id", id);
        }
        final String sql = useName ? getQuerySqlUseNameNoneLock() : getQuerySqlNoneLock();
        final FastreeEntity parentEntity =
                jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                    FastreeEntity entity = new FastreeEntity();
                    entity.setKind(rs.getString(1));
                    entity.setRgtNo(rs.getInt(2));
                    entity.setLftNo(rs.getInt(3));
                    return entity;
                });
        //创建失败
        if (Objects.isNull(parentEntity)) {
            return -1;
        }
        final Map<String, Object> params2 = new HashMap<>();
        params2.put("lftno", parentEntity.getLftNo());
        params2.put("rgtno", parentEntity.getRgtNo());
        params2.put("kind", parentEntity.getKind());

        return jdbcTemplate.queryForObject(getQuerySqlWithLevel(), params2, (rs, rowNum) -> rs.getInt(1));
    }

    private List<FastreeEntity> queryNodes(Integer id, String name, boolean parent) {
        final boolean useName = StringUtils.hasText(name);
        final Map<String, Object> params = new HashMap<>();
        if (useName) {
            params.put("name", name);
        } else {
            params.put("id", id);
        }
        final String sql = useName ? getQuerySqlUseNameNoneLock() : getQuerySqlNoneLock();
        final FastreeEntity parentEntity =
                jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                    FastreeEntity entity = new FastreeEntity();
                    entity.setKind(rs.getString(1));
                    entity.setRgtNo(rs.getInt(2));
                    entity.setLftNo(rs.getInt(3));
                    return entity;
                });
        //创建失败
        if (Objects.isNull(parentEntity)) {
            return Collections.emptyList();
        }
        final Map<String, Object> params2 = new HashMap<>();
        params2.put("lftno", parentEntity.getLftNo());
        params2.put("rgtno", parentEntity.getRgtNo());
        params2.put("kind", parentEntity.getKind());

        final String sql2 = parent ? getQuerySqlWithParent() : getQuerySqlWithChildren();

        return jdbcTemplate.query(sql2, params2, (rs, rowNum) -> {
            final FastreeEntity entity = new FastreeEntity();
            entity.setKind(rs.getString(1));
            entity.setRgtNo(rs.getInt(2));
            entity.setLftNo(rs.getInt(3));
            entity.setName(rs.getString(4));
            entity.setId(rs.getInt(5));
            final String columns = fastreeProperties.getColumns();
            if (StringUtils.hasText(columns)) {
                final Map<String, Object> custom = new HashMap<>();
                final String[] cs = columns.split(",");
                for (int i = 0; i < cs.length; i++) {
                    if (StringUtils.hasText(cs[i])) {
                        custom.put(cs[i], rs.getObject(i + 6));
                    }
                }
                entity.setCustom(custom);
            }
            return entity;
        });
    }

    private String getQuerySql() {
        return "SELECT kind, rgtno, lftno, name, id FROM " + sqlStatementsSource.tableName() +" WHERE id = :id FOR UPDATE";
    }

    private String getQuerySqlUseName() {
        return "SELECT kind, rgtno, lftno, name, id FROM " + sqlStatementsSource.tableName() +" WHERE name = :name FOR UPDATE";
    }

    private String getQuerySqlNoneLock() {
        return "SELECT kind, rgtno, lftno, name, id FROM " + sqlStatementsSource.tableName() +" WHERE id = :id";
    }

    private String getQuerySqlUseNameNoneLock() {
        return "SELECT kind, rgtno, lftno, name, id FROM " + sqlStatementsSource.tableName() +" WHERE name = :name";
    }

    private String getQuerySqlWithChildren() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT kind, rgtno, lftno, name, id");
        if (StringUtils.hasText(fastreeProperties.getColumns())) {
            sb.append("," + fastreeProperties.getColumns());
        }
        //lft BETWEEN lftid AND rgtid
        return sb.append(" FROM " + sqlStatementsSource.tableName() +" WHERE kind = :kind AND lftno >= :lftno AND lftno <= :rgtno ORDER BY lftno ASC").toString();
    }

    private String getQuerySqlWithParent() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT kind, rgtno, lftno, name, id");
        if (StringUtils.hasText(fastreeProperties.getColumns())) {
            sb.append("," + fastreeProperties.getColumns());
        }
        //lft < lftid AND rgt > rgtid ORDER BY lft ASC;
        return sb.append(" FROM " + sqlStatementsSource.tableName() +" WHERE kind = :kind AND lftno < :lftno AND rgtno > :rgtno ORDER BY lftno ASC").toString();
    }

    private String getQuerySqlWithLevel() {
        //SELECT COUNT(*) INTO result  FROM tree WHERE lft <= lftid AND rgt >= rgtid;
        return "SELECT count(1) FROM " + sqlStatementsSource.tableName() +" WHERE kind = :kind AND lftno <= :lftno AND rgtno >= :rgtno";
    }

    private String getUpdateRgtNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgtno = rgtno + 2 WHERE kind = :kind AND rgtno >= :rgtno";
    }

    private String getUpdateLftNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lftno = lftno + 2 WHERE kind = :kind AND lftno >= :rgtno";
    }

    private String getRemoveLftNoSql() {
        //UPDATE tree SET lft = lft -(rgtid - lftid  + 1) WHERE lft > lftid;
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lftno = lftno - (:rgtno - :lftno + 1) WHERE kind = :kind AND lftno > :lftno";
    }

    private String getRemoveRgtNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgtno = rgtno - (:rgtno - :lftno + 1) WHERE kind = :kind AND rgtno > :rgtno";
    }

    private String getDeleteSql() {
        return "DELETE FROM " + sqlStatementsSource.tableName() + " WHERE kind = :kind AND lftno >= :lftno AND rgtno <= :rgtno";
    }

    private String getInsertSql() {
        return "INSERT INTO " + sqlStatementsSource.tableName() + "(kind, `name`, lftno, rgtno) VALUES (:kind, :name, :lftno, :rgtno)";
    }
}
