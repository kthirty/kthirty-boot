package top.kthirty.core.db.permission;

import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.dialect.impl.CommonsDialectImpl;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.table.TableInfo;

public class DataPermissionDialectImpl extends CommonsDialectImpl{
    @Override
    public void prepareAuth(QueryWrapper queryWrapper, OperateType operateType) {

        super.prepareAuth(queryWrapper, operateType);
    }

    @Override
    public void prepareAuth(String schema, String tableName, StringBuilder sql, OperateType operateType) {
        super.prepareAuth(schema, tableName, sql, operateType);
    }

    @Override
    public void prepareAuth(TableInfo tableInfo, StringBuilder sql, OperateType operateType) {
        super.prepareAuth(tableInfo, sql, operateType);
    }
}
