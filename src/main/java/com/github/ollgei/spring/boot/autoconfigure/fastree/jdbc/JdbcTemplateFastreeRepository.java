package com.github.ollgei.spring.boot.autoconfigure.fastree.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeEntity;
import com.github.ollgei.spring.boot.autoconfigure.fastree.core.FastreeKeyEntity;
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

    private String columns = "";

    public JdbcTemplateFastreeRepository(String tableName, @NonNull JdbcTemplate jdbcTemplate) {
        this(tableName, jdbcTemplate, null);
    }

    public JdbcTemplateFastreeRepository(String tableName, @NonNull JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        super(tableName, jdbcTemplate, transactionManager);
    }

    @Override
    public List<FastreeEntity> queryWithChildren(Integer id) {
        return queryWithId(id, false);
    }

    @Override
    public List<FastreeEntity> queryWithChildren(FastreeKeyEntity key) {
        return queryWithCode(key, false);
    }

    @Override
    public List<FastreeEntity> queryWithParent(FastreeKeyEntity key) {
        return queryWithCode(key, true);
    }

    @Override
    public List<FastreeEntity> queryWithParent(Integer id) {
        return queryWithId(id, true);
    }

    @Override
    public FastreeEntity queryParent(Integer id) {
        return queryParent(id, null);
    }

    @Override
    public FastreeEntity queryParent(FastreeKeyEntity keyEntity) {
        return queryParent(null, keyEntity);
    }

    @Override
    public Integer queryLevel(Integer id) {
        return queryLevel(id, null);
    }

    @Override
    public Integer queryLevel(FastreeKeyEntity keyEntity) {
        return queryLevel(null, keyEntity);
    }

    @Override
    public FastreeEntity save(FastreeKeyEntity key, String code, Map<String, Object> custom) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("code", key.getCode());
            params.put("gpname", key.getGpname());
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySqlUseCode(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setGpname(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return null;
            }

            jdbcTemplate.query(getQuerySqlUseCode(), params, (rs, rowNum) -> {
                FastreeEntity entity = new FastreeEntity();
                entity.setGpname(rs.getString(1));
                entity.setRgtNo(rs.getInt(2));
                return entity;
            });
            return saveWithTransaction(parentEntity.getGpname(), parentEntity.getRgtNo(), code, custom);
        });
    }

    @Override
    public FastreeEntity save(Integer pid, String code, Map<String, Object> custom) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("id", pid);
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySql(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setGpname(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return null;
            }
            return saveWithTransaction(parentEntity.getGpname(), parentEntity.getRgtNo(), code, custom);
        });
    }

    @Override
    public FastreeEntity init(FastreeKeyEntity key, Map<String, Object> custom) {
        return transactionTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            //insert
            final Map<String, Object> params3 = new HashMap<>();
            params3.put("gpname", key.getGpname());
            params3.put("code", key.getCode());
            params3.put("lftno", 1);
            params3.put("rgtno", 2);
            params3.putAll(custom);
            int insertedRows = jdbcTemplate.update(getInsertSql(custom), new MapSqlParameterSource(params3), keyHolder);
            if (insertedRows != 1) {
                throw new RuntimeException("插入失败");
            }
            final FastreeEntity root = new FastreeEntity();
            root.setId(keyHolder.getKey().intValue());
            root.setCode(key.getCode());
            root.setGpname(key.getGpname());
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
                        entity.setGpname(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        entity.setLftNo(rs.getInt(3));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }

            return removeWithTransaction(parentEntity.getGpname(), parentEntity.getLftNo(), parentEntity.getRgtNo());
        });
    }

    @Override
    public Boolean remove(FastreeKeyEntity keyEntity) {
        return transactionTemplate.execute(status -> {
            final Map<String, Object> params = new HashMap<>();
            params.put("code", keyEntity.getCode());
            params.put("gpname", keyEntity.getGpname());
            final FastreeEntity parentEntity =
                    jdbcTemplate.queryForObject(getQuerySqlUseCode(), params, (rs, rowNum) -> {
                        FastreeEntity entity = new FastreeEntity();
                        entity.setGpname(rs.getString(1));
                        entity.setRgtNo(rs.getInt(2));
                        entity.setLftNo(rs.getInt(3));
                        return entity;
                    });
            //创建失败
            if (Objects.isNull(parentEntity)) {
                return false;
            }

            return removeWithTransaction(parentEntity.getGpname(), parentEntity.getLftNo(), parentEntity.getRgtNo());
        });
    }

    private FastreeEntity saveWithTransaction(String gpname, Integer rgtNo, String code, Map<String, Object> custom) {
        //update
        final Map<String, Object> params1 = new HashMap<>();
        params1.put("rgtno", rgtNo);
        params1.put("gpname", gpname);
        boolean result = jdbcTemplate.update(getUpdateRgtNoSql(), params1) > 0;
        if (!result) {
            throw new RuntimeException("更新失败");
        }
        result = jdbcTemplate.update(getUpdateLftNoSql(), params1) > 0;
        if (!result) {
            log.warn("no found for lftno >= {}", rgtNo);
        }
        //insert
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final Map<String, Object> params2 = new HashMap<>();
        params2.put("gpname", gpname);
        params2.put("code", code);
        params2.put("lftno", rgtNo);
        params2.put("rgtno", rgtNo + 1);
        params2.putAll(custom);
        int insertedRows = jdbcTemplate.update(getInsertSql(custom), new MapSqlParameterSource(params2), keyHolder);
        if (insertedRows != 1) {
            throw new RuntimeException("插入失败");
        }

        final FastreeEntity entity = new FastreeEntity();
        entity.setId(keyHolder.getKey().intValue());
        entity.setCode(code);
        entity.setGpname(gpname);
        entity.setLftNo(rgtNo);
        entity.setLftNo(rgtNo + 1);
        return entity;
    }

    private Boolean removeWithTransaction(String gpname, Integer lftNo, Integer rgtNo) {
        final Map<String, Object> params1 = new HashMap<>();
        params1.put("rgtno", rgtNo);
        params1.put("lftno", lftNo);
        params1.put("gpname", gpname);
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

    private List<FastreeEntity> queryWithCode(FastreeKeyEntity key, boolean parent) {
        return queryNodes(null, key, parent);
    }

    private List<FastreeEntity> queryWithId(Integer id, boolean parent) {
        return queryNodes(id, null, parent);
    }

    private Integer queryLevel(Integer id, FastreeKeyEntity keyEntity) {
        final Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(keyEntity)) {
            params.put("code", keyEntity.getCode());
            params.put("gpname", keyEntity.getGpname());
        } else {
            params.put("id", id);
        }
        final String sql = Objects.nonNull(keyEntity) ? getQuerySqlUseCodeNoneLock() : getQuerySqlNoneLock();
        final FastreeEntity parentEntity =
                jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                    FastreeEntity entity = new FastreeEntity();
                    entity.setGpname(rs.getString(1));
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
        params2.put("gpname", parentEntity.getGpname());

        return jdbcTemplate.queryForObject(getQuerySqlWithLevel(), params2, (rs, rowNum) -> rs.getInt(1));
    }

    private FastreeEntity queryParent(Integer id, FastreeKeyEntity keyEntity) {
        final Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(keyEntity)) {
            params.put("code", keyEntity.getCode());
            params.put("gpname", keyEntity.getGpname());
        } else {
            params.put("id", id);
        }
        final String sql = Objects.nonNull(keyEntity) ? getQuerySqlUseCodeNoneLock() : getQuerySqlNoneLock();
        final List<FastreeEntity> parentEntities =
                jdbcTemplate.query(sql, params, (rs, rowNum) -> {
                    FastreeEntity entity = new FastreeEntity();
                    entity.setGpname(rs.getString(1));
                    entity.setRgtNo(rs.getInt(2));
                    entity.setLftNo(rs.getInt(3));
                    return entity;
                });
        //创建失败
        if (Objects.isNull(parentEntities)) {
            return null;
        }
        if (parentEntities.size() != 1) {
            log.warn("found parentEntities size {}", parentEntities.size());
            return null;
        }

        FastreeEntity parentEntity = parentEntities.get(0);
        final Map<String, Object> params2 = new HashMap<>();
        params2.put("lftno", parentEntity.getLftNo());
        params2.put("rgtno", parentEntity.getRgtNo());
        params2.put("gpname", parentEntity.getGpname());

        //EmptyResultDataAccessException IncorrectResultSizeDataAccessException
        final List<FastreeEntity> list = jdbcTemplate.query(getQueryParentSql(), params2,
                (rs, rowNum) -> mapToFastreeEntity(rs));
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        log.warn("found many data {} for {}", list.size(), Objects.nonNull(keyEntity) ? keyEntity : id);
        return null;
    }

    private FastreeEntity mapToFastreeEntity(ResultSet rs) throws SQLException {
        final FastreeEntity entity = new FastreeEntity();
        entity.setGpname(rs.getString(1));
        entity.setRgtNo(rs.getInt(2));
        entity.setLftNo(rs.getInt(3));
        entity.setCode(rs.getString(4));
        entity.setId(rs.getInt(5));
        if (StringUtils.hasText(this.columns)) {
            final Map<String, Object> custom = new HashMap<>();
            final String[] cs = this.columns.split(",");
            for (int i = 0; i < cs.length; i++) {
                if (StringUtils.hasText(cs[i])) {
                    custom.put(cs[i].trim(), rs.getObject(i + 6));
                }
            }
            entity.setCustom(custom);
        }
        return entity;
    }

    private List<FastreeEntity> queryNodes(Integer id, FastreeKeyEntity key, boolean parent) {
        final Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(key)) {
            params.put("code", key.getCode());
            params.put("gpname", key.getGpname());
        } else {
            params.put("id", id);
        }
        final String sql = Objects.nonNull(key) ? getQuerySqlUseCodeNoneLock() : getQuerySqlNoneLock();
        final FastreeEntity parentEntity =
                jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                    FastreeEntity entity = new FastreeEntity();
                    entity.setGpname(rs.getString(1));
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
        params2.put("gpname", parentEntity.getGpname());

        final String sql2 = parent ? getQuerySqlWithParent() : getQuerySqlWithChildren();

        return jdbcTemplate.query(sql2, params2, (rs, rowNum) -> {
            final FastreeEntity entity = new FastreeEntity();
            entity.setGpname(rs.getString(1));
            entity.setRgtNo(rs.getInt(2));
            entity.setLftNo(rs.getInt(3));
            entity.setCode(rs.getString(4));
            entity.setId(rs.getInt(5));
            if (StringUtils.hasText(this.columns)) {
                final Map<String, Object> custom = new HashMap<>();
                final String[] cs = this.columns.split(",");
                for (int i = 0; i < cs.length; i++) {
                    if (StringUtils.hasText(cs[i])) {
                        custom.put(cs[i].trim(), rs.getObject(i + 6));
                    }
                }
                entity.setCustom(custom);
            }
            return entity;
        });
    }

    private String getQuerySql() {
        return "SELECT gpname, rgtno, lftno, code, id FROM " + sqlStatementsSource.tableName() +" WHERE id = :id FOR UPDATE";
    }

    private String getQuerySqlUseCode() {
        return "SELECT gpname, rgtno, lftno, code, id FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND code = :code FOR UPDATE";
    }

    private String getQuerySqlNoneLock() {
        return "SELECT gpname, rgtno, lftno, code, id FROM " + sqlStatementsSource.tableName() +" WHERE id = :id";
    }

    private String getQuerySqlUseCodeNoneLock() {
        return "SELECT gpname, rgtno, lftno, code, id FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND code = :code";
    }

    private String getQueryParentSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT gpname, rgtno, lftno, code, id");
        if (StringUtils.hasText(this.columns)) {
            sb.append("," + this.columns);
        }
        //lft < lftid AND rgt > rgtid ORDER BY lft ASC;
        return sb.append(
                " FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND lftno < :lftno AND rgtno > :rgtno ORDER BY lftno DESC limit 1")
                .toString();
    }

    private String getQuerySqlWithChildren() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT gpname, rgtno, lftno, code, id");
        if (StringUtils.hasText(this.columns)) {
            sb.append("," + this.columns);
        }
        //lft BETWEEN lftid AND rgtid
        return sb.append(" FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND lftno >= :lftno AND lftno <= :rgtno ORDER BY lftno ASC").toString();
    }

    private String getQuerySqlWithParent() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT gpname, rgtno, lftno, code, id");
        if (StringUtils.hasText(this.columns)) {
            sb.append("," + this.columns);
        }
        //lft < lftid AND rgt > rgtid ORDER BY lft ASC;
        return sb.append(" FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND lftno < :lftno AND rgtno > :rgtno ORDER BY lftno ASC").toString();
    }

    private String getQuerySqlWithLevel() {
        //SELECT COUNT(*) INTO result  FROM tree WHERE lft <= lftid AND rgt >= rgtid;
        return "SELECT count(1) FROM " + sqlStatementsSource.tableName() +" WHERE gpname = :gpname AND lftno <= :lftno AND rgtno >= :rgtno";
    }

    private String getUpdateRgtNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgtno = rgtno + 2 WHERE gpname = :gpname AND rgtno >= :rgtno";
    }

    private String getUpdateLftNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lftno = lftno + 2 WHERE gpname = :gpname AND lftno >= :rgtno";
    }

    private String getRemoveLftNoSql() {
        //UPDATE tree SET lft = lft -(rgtid - lftid  + 1) WHERE lft > lftid;
        return "UPDATE " + sqlStatementsSource.tableName() + " SET lftno = lftno - (:rgtno - :lftno + 1) WHERE gpname = :gpname AND lftno > :lftno";
    }

    private String getRemoveRgtNoSql() {
        return "UPDATE " + sqlStatementsSource.tableName() + " SET rgtno = rgtno - (:rgtno - :lftno + 1) WHERE gpname = :gpname AND rgtno > :rgtno";
    }

    private String getDeleteSql() {
        return "DELETE FROM " + sqlStatementsSource.tableName() + " WHERE gpname = :gpname AND lftno >= :lftno AND rgtno <= :rgtno";
    }

    private String getInsertSql(Map<String, Object> custom) {
        final StringBuilder cols = new StringBuilder();
        final StringBuilder vals = new StringBuilder();
        cols.append("gpname, `code`, lftno, rgtno");
        vals.append(":gpname, :code, :lftno, :rgtno");
        if (StringUtils.hasText(this.columns)) {
            String[] arr = this.columns.split(",");
            for (int i = 0; i < arr.length; i++) {
                if (StringUtils.hasText(arr[i])) {
                    final String key = arr[i].trim();
                    if (custom.containsKey(key)) {
                        cols.append(",").append(key);
                        vals.append(", :").append(key);
                    }
                }

            }
        }
        return "INSERT INTO " + sqlStatementsSource.tableName() + "(" + cols.toString() +") VALUES (" + vals.toString() + ")";
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
