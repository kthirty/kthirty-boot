package top.kthirty.core.tool.excel.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.Dict;
import top.kthirty.core.tool.dict.DictUtil;

import java.lang.reflect.Field;

public class DictCellWriter implements CellWriter{
    @Override
    public Object edit(Object obj, String fieldName) {
        // 实体为空无法获取
        if(ObjUtil.isNull(obj)){
            return null;
        }
        // 不包含字段无法获取
        if(!ReflectUtil.hasField(obj.getClass(),fieldName)){
            return null;
        }
        Field field = ReflectUtil.getField(obj.getClass(), fieldName);
        Object fieldValue = ReflectUtil.getFieldValue(obj, field);
        // 数据字典可用,且字段包含Dict注解，使用数据字典解析为Label
        if(AnnotationUtil.hasAnnotation(field, Dict.class) && DictUtil.isAvailable()){
            Dict dict = AnnotationUtil.getAnnotation(field, Dict.class);
            String code = StrUtil.blankToDefault(dict.code(), fieldName);
            String label = DictUtil.getLabel(code, Func.toStr(fieldValue));
            if(StrUtil.isNotBlank(label)){
                return label;
            }
        }
        return fieldValue;
    }
}
