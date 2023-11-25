package top.kthirty.core.db.liquibase;

import liquibase.changelog.IncludeAllFilter;

public class KthirtyIncludeAllFilter implements IncludeAllFilter {
    @Override
    public boolean include(String changeLogPath) {
        // includeAll 不引用主文件
        return !"db/changelog/db.changelog-master.yaml".equals(changeLogPath);
    }
}
