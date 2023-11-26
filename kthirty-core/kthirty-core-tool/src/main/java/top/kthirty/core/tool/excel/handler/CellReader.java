package top.kthirty.core.tool.excel.handler;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.cell.CellEditor;

/**
 * <p>
 * 写入文件时的Cell值修改
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public interface CellReader extends CellEditor {
    /**
     * 初始化
     * @author Kthirty
     * @since 2023/11/26
     * @param headerRowIndex 标题行号
     * @param reader 当前读取器
     * @param clazz 实体
     */
    void init(int headerRowIndex, ExcelReader reader, Class<?> clazz);
}
