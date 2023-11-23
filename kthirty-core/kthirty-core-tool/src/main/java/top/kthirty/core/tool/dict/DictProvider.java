package top.kthirty.core.tool.dict;

import java.util.List;

/**
 * <p>
 * 数据字典解析器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/22
 */
public interface DictProvider {
    /**
     * <p>
     * 修改时删除相应的缓存
     * </p>
     *
     * @author KThirty
     * @since 2023/11/22
     */
    void removeCache(String code,String value);
    default void removeCache(String code){removeCache(code,null);}

    /**
     * 添加缓存
     * @param code 字典代码
     * @param items 选项
     * @param time 过期时间-秒
     */
    void put(String code, List<DictItem> items,long time);

    /**
     * 获取字典
     * @param code 字典code
     * @return 选项列表
     */
    List<DictItem> get(String code);
}
