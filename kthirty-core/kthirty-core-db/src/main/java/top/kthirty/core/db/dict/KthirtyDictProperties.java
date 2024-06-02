package top.kthirty.core.db.dict;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@ToString
@RefreshScope
@ConfigurationProperties("kthirty.dict")
public class KthirtyDictProperties {
    /**
     * 数据字典查询sql
     */
    private String dictSql = "select id,parent_id,label,value from sys_dict_item where code = ?";
    /**
     * 缓存时间，默认两小时
     */
    private long cacheTime = 2 * 60 * 60;
    /**
     * 查库为空缓存时间
     */
    private long emptyCache = 5 * 60;
    /**
     * redis Key 前缀
     */
    private String cacheKeyPrefix = "sys:dict:";
    /**
     * 是否自动查数据库
     */
    private boolean autoDb = true;
}
