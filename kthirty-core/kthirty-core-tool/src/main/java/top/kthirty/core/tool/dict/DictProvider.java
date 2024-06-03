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
    void removeCache(String code);

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
    /**
     * 获取标签
     * @param code 字典代码
     * @param value 字典值
     * @param separator 分割符号(多个值分割后获取)
     * @return 标签
     */
    String getLabel(String code,String value,String separator);
    /**
     * 标签解析为值
     * @param code 字典代码
     * @param label 标签
     * @param separator 分割符号(多个值分割后获取)
     * @return 字典值
     */
    String getValue(String code,String label,String separator);
}
