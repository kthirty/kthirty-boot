package top.kthirty.core.db.permission.support;

import lombok.Data;

@Data
public class DataPermission {
    /**
     * 限制范围类型
     */
    private ScopeType scopeType;
    /**
     * 方法名或表名
     */
    private String scopeValue;

    /**
     * 操作类型 （SELECT/UPDATE/DELETE/INSERT）
     */
    private String operateType;

    private String script;
}
