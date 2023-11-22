package top.kthirty.core.tool.dict;
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
     * 获取数据字典
     * @param code 字典code
     * @param value 值
     * @return label
     */
    String getLabel(String code,String value);

    /**
     * 获取表数据
     * @param tableName 表名
     * @param valueField 值对应的字段明
     * @param labelField 需要获取的label字段名
     * @param value 属性值
     * @return label
     */
    String getLabel(String tableName,String valueField,String labelField,String value);
}
