package top.kthirty.core.tool.excel.handler;
/**
 * <p>
 * 写入文件时的Cell值修改
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public interface CellWriter {
    /**
     * 修改Cell写入值
     * @author Kthirty
     * @since 2023/11/26
     * @param obj 实体
     * @param fieldName 字段名称
     * @return java.lang.Object
     */
    Object edit(Object obj, String fieldName);
}
