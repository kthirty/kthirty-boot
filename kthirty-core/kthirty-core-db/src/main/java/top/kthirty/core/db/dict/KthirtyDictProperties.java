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
    private String dictSql = "select label from sys_dict_item where code = '?' and value = '?'";
    /**
     * 缓存时间，默认两小时
     */
    private long cacheTime = 60 * 2;
    /**
     * redis Key 前缀
     */
    private String redisKeyPrefix = "sys:dict:";
}
