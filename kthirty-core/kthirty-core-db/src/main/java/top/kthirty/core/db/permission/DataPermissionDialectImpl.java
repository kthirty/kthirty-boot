package top.kthirty.core.db.permission;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.table.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DataPermissionDialectImpl extends CommonsDialectImpl{
    /**
     * QueryWrapper 类查询
     * @param queryWrapper queryWrapper
     * @param operateType  操作类型
     */
    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {
        DataPermissionHolder.get()
                .setOperateType(operateType)
                .setTables(CPI.getQueryTables(queryWrapper));
        super.prepareAuth(queryWrapper, operateType);
    }


    /**
     * @see Db#selectOneByCondition(String, String, QueryCondition)
     * @param schema      schema
     * @param tableName   表名
     * @param sql         sql
     * @param operateType 操作类型
     */
    @Override
    public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
        DataPermissionHolder.get()
                .setOperateType(operateType)
                .setTables(List.of(new QueryTable(schema,tableName)));



        super.prepareAuth(schema, tableName, sql, operateType);
    }

    /**
     * entity 类的没有queryWrapper也没有tableName会使用mapper的context获取表信息
     * @see BaseMapper#insertWithPk(Object, boolean)
     * @param tableInfo   tableInfo
     * @param sql         sql
     * @param operateType 操作类型
     */
    @Override
    public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
        DataPermissionHolder.get()
                .setOperateType(operateType)
                .setTables(List.of(new QueryTable(tableInfo.getSchema(),tableInfo.getTableName())));

        super.prepareAuth(tableInfo, sql, operateType);
    }
}
