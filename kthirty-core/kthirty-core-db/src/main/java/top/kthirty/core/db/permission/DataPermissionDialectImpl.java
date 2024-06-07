package top.kthirty.core.db.permission;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.row.RowMapper;
import com.mybatisflex.core.table.TableInfo;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.boot.secure.SecureUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Slf4j
public class DataPermissionDialectImpl extends CommonsDialectImpl {
    /**
     * QueryWrapper 类查询
     *
     * @param queryWrapper queryWrapper
     * @param operateType  操作类型
     * @see BaseMapper#deleteByQuery(QueryWrapper)
     * @see BaseMapper#updateByQuery(Object, boolean, QueryWrapper)
     * @see BaseMapper#deleteByQuery(QueryWrapper)
     * @see BaseMapper#selectListByQuery(QueryWrapper)
     * @see BaseMapper#selectObjectListByQuery(QueryWrapper)
     * @see RowMapper#deleteByQuery(String, String, QueryWrapper)
     * @see RowMapper#updateByQuery(String, String, Row, QueryWrapper)
     * @see RowMapper#selectListByQuery(String, String, QueryWrapper)
     * @see RowMapper#selectObjectListByQuery(String, String, QueryWrapper)
     */
    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        DataPermissionHolder.getContext()
                .setOperateType(operateType)
                .setTables(CPI.getQueryTables(queryWrapper))
                .setCurrentUser(SecureUtil.getCurrentUser());

        super.prepareAuth(queryWrapper, operateType);
    }


    /**
     * @param schema      schema
     * @param tableName   表名
     * @param sql         sql
     * @param operateType 操作类型
     * @see RowMapper#deleteById(String, String, String, Object)
     * @see RowMapper#deleteBatchByIds(String, String, String, Collection)
     * @see RowMapper#updateBatchById(String, String, List)
     * @see RowMapper#selectOneById(String, String, String, Object)
     */
    @Override
    public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
        super.prepareAuth(schema, tableName, sql, operateType);
    }

    /**
     * entity 类的没有queryWrapper也没有tableName会使用mapper的context获取表信息
     *
     * @param tableInfo   tableInfo
     * @param sql         sql
     * @param operateType 操作类型
     * @see BaseMapper#deleteById(Serializable)
     * @see BaseMapper#deleteBatchByIds(Collection)
     * @see BaseMapper#deleteBatchByIds(List, int)
     * @see BaseMapper#update(Object, boolean)
     * @see BaseMapper#selectOneById(Serializable)
     * @see BaseMapper#selectListByIds(Collection)
     * @see RowMapper#updateEntity(Object)
     */
    @Override
    public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
        super.prepareAuth(tableInfo, sql, operateType);
    }
}
