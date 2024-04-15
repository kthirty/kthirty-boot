package top.kthirty.core.tool.excel.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Cell;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.Dict;
import top.kthirty.core.tool.dict.DictUtil;

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
    private Func1<Cell,Field> getFieldFunc;

    @Override
    public Object edit(Cell cell, Object value) {
        // 读取标题
        if(getFieldFunc != null && DictUtil.isAvailable()){
            Field field = getFieldFunc.callWithRuntimeException(cell);
            if(field != null){
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

    @Override
    public void init(Func1<Cell, Field> getFieldFunc) {
        this.getFieldFunc = getFieldFunc;
    }
}
