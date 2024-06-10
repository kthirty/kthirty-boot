package top.kthirty.core.db.permission.support;

import cn.hutool.core.util.StrUtil;

public class DataPermissionHelper {

    /**
     * 不包含AND的sql语句
     * @return
     */
    public static String toSql(StringBuilder stringBuilder){
        String sql = StrUtil.trim(stringBuilder.toString());
        if(StrUtil.startWithAnyIgnoreCase(sql, "and")){
            sql =  StrUtil.removePrefix(sql, "and");
        }
        return sql;
    }
}
