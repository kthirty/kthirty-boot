package top.kthirty.core.db.permission.support;

public enum ScopeType {
    /**
     * Mapper的方法
     */
    METHOD,
    /**
     * 表名
     */
    TABLE;

    public static ScopeType match(String value){
        for (ScopeType scopeType : values()) {
            if(scopeType.name().equalsIgnoreCase(value)){
                return scopeType;
            }
        }
        return null;
    }
}
