package top.kthirty.core.db.permission.support;

import lombok.Data;

@Data
public class DataPermission {
    /**
     * 规则编码
     */
    private String code;
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
    /**
     * 处理类型
     */
    private HandleType handleType;
    /**
     * 处理内容/预设规则名称
     */
    private String handleContent;
    /**
     * 脚本参数
     */
    private String handleParam;
}
