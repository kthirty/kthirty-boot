package top.kthirty.core.db.liquibase;

import liquibase.changelog.IncludeAllFilter;

public class IgnoreMasterFilter implements IncludeAllFilter {
    @Override
    public boolean include(String changeLogPath) {
        // includeAll 不引用主文件
        return !changeLogPath.contains("changelog-master");
    }
}
