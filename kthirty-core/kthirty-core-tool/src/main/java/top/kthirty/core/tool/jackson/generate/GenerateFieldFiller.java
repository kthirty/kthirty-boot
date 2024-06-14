package top.kthirty.core.tool.jackson.generate;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import top.kthirty.core.tool.jackson.filler.JsonFillGetter;
import top.kthirty.core.tool.jackson.filler.JsonFiller;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @description 自动根据Collection或者Object的属性生成一个Json字段
 * @author KThirty
 * @since 2024/6/14 16:41
 */
public interface GenerateFieldFiller extends JsonFiller {
    @JsonFillGetter
    default Map<String,Object> __extraJsonField(){
        Map<String,Object> result = new HashMap<>();
        Field[] fields = ReflectUtil.getFields(this.getClass(), it -> AnnotationUtil.hasAnnotation(it, GenerateField.class));
        for (Field field : fields) {
            try{
                GenerateField generateField = AnnotationUtil.getAnnotation(field, GenerateField.class);
                Object fieldValue = ReflectUtil.getFieldValue(this, field);
                if(fieldValue == null){
                    result.put(generateField.genField(),"");
                    continue;
                }
                if(Collection.class.isAssignableFrom(field.getType())){
                    Collection<?> valueColl = ((Collection<?>)fieldValue);
                    if(CollUtil.isEmpty(valueColl)){
                        result.put(generateField.genField(),"");
                        continue;
                    }
                    String value = valueColl.stream().map(it -> StrUtil.toString(ReflectUtil.getFieldValue(it,generateField.objField()))).collect(Collectors.joining(","));
                    result.put(generateField.genField(),value);
                }else{
                    String value = StrUtil.toString(ReflectUtil.getFieldValue(fieldValue, generateField.objField()));
                    result.put(generateField.genField(),value);
                }
            }catch (Throwable e){
                StaticLog.warn("GenerateFieldFiller 处理失败",e);
            }
        }
        return result;
    }
}
