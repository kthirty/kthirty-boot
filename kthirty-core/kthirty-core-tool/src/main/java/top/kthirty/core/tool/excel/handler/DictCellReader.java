package top.kthirty.core.tool.excel.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.Dict;
import top.kthirty.core.tool.dict.DictUtil;
import top.kthirty.core.tool.excel.Excel;
import top.kthirty.core.tool.excel.support.ExcelHelper;

import java.lang.reflect.Field;

/**
 * <p>
 * 数据字典解析为Value
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public class DictCellReader implements CellReader {
    private int headerRowIndex;
    private ExcelReader reader;
    private Class<?> clazz;

    @Override
    public void init(int headerRowIndex,ExcelReader reader,Class<?> clazz){
        this.headerRowIndex = headerRowIndex;
        this.reader = reader;
        this.clazz = clazz;
    }
    @Override
    public Object edit(Cell cell, Object value) {
        // 读取标题
        Object headerCellValue = CellUtil.getCellValue(reader.getCell(cell.getColumnIndex(),headerRowIndex));
        if(headerCellValue.getClass() == String.class){
            String title = Func.toStr(headerCellValue);
            // 获取对应的字段并包含Dict注解
            Field[] fields = ReflectUtil.getFields(clazz, field ->
                    AnnotationUtil.hasAnnotation(field, Excel.class)
                            && ExcelHelper.getFieldTitle(field).equals(title)
                            && AnnotationUtil.hasAnnotation(field, Dict.class)
            );
            // 解析Dict Value
            if(Func.isNotEmpty(fields) && DictUtil.isAvailable()){
                Field field = fields[0];
                Dict dict = AnnotationUtil.getAnnotation(field, Dict.class);
                String code = StrUtil.blankToDefault(dict.code(), field.getName());
                String dictValue = DictUtil.getValue(code, Func.toStr(value));
                if(Func.isNotBlank(dictValue)){
                    return dictValue;
                }
            }
        }
        return value;
    }
}
