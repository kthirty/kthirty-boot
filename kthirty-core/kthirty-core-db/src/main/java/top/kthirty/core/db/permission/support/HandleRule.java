package top.kthirty.core.db.permission.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.kthirty.core.tool.utils.StringPool;

@AllArgsConstructor
@Getter
public enum HandleRule {
    /**
     * 查询当前登录人创建的数据
     */
    QUERY_CURRENT_USER_DATA("""
                #(context.sql.append(" and ").append(param).append(" = ").append(context?.currentUser?.username ?? "''"))
                 """),
    /**
     * 直接拼接SQL
     */
    APPEND_SQL("#(context.sql.append(param))");
    private final String script;

    public static String getScript(String name){
        for (HandleRule value : values()) {
            if(value.name().equals(name)){
                return value.getScript();
            }
        }
        return StringPool.EMPTY;
    }
}
