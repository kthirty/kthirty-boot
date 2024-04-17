package top.kthirty.core.tool.excel.handler;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.cell.CellEditor;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

/**
 * <p>
 * 写入文件时的Cell值修改
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public abstract class CellReader implements CellEditor {
    protected Func1<Cell,Field> getFieldFunc;

    /**
     * 初始化
     *
     * @param getFieldFunc 通过单元格获取对应的字段
     * @author Kthirty
     * @since 2023/11/26
     */
    public void init(Func1<Cell, Field> getFieldFunc) {
        this.getFieldFunc = getFieldFunc;
    }
}
