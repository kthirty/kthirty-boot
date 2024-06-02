package top.kthirty.core.tool.dict;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import top.kthirty.core.tool.Func;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>
 * Class实现此接口用于支持数据字典自动填充
 * </p>
 * @see top.kthirty.core.tool.dict.Dict
 * @author KThirty
 * @since 2023/11/22
 */
public interface DictFiller {

    @JsonAnyGetter
    default Map<String, String> jsonAnyGetter() {
        Map<String, String> map = new HashMap<>();
        try{
            // 字段上的注解
            Arrays.stream(ReflectUtil.getFields(getClass()))
                    .filter(field -> AnnotationUtil.hasAnnotation(field, Dict.class))
                    .forEach(field -> {
                        Dict dict = AnnotationUtil.getAnnotation(field, Dict.class);
                        String code = StrUtil.blankToDefault(dict.code(), field.getName());
                        String value = Func.toStr(ReflectUtil.getFieldValue(this, field));
                        String fieldName = StrUtil.format(dict.fieldName(), field.getName());
                        if (Func.isNoneBlank(value, fieldName,dict.fieldName())) {
                            map.put(fieldName, DictUtil.getLabel(code, value,dict.splitBy()));
                        }
                    });
            // 无参数有返回值方法，且包含数据字典注解
            Arrays.stream(ReflectUtil.getMethods(getClass(), m ->
                            AnnotationUtil.hasAnnotation(m, Dict.class)
                                    && m.getParameterCount() == 0
                                    && !Void.TYPE.equals(m.getReturnType())
                                    && ClassUtil.isJdkClass(m.getReturnType())
                    ))
                    .forEach(method -> {
                        String name = StrUtil.startWith(method.getName(),"get") ? StrUtil.lowerFirst(StrUtil.removePrefix(method.getName(),"get")) : method.getName();
                        Dict dict = AnnotationUtil.getAnnotation(method, Dict.class);
                        String code =StrUtil.blankToDefault(dict.code(), method.getName());
                        String value = Func.toStr(ReflectUtil.invoke(this,method));
                        String fieldName = StrUtil.format(dict.fieldName(), name);
                        if (Func.isNoneBlank(value, fieldName)) {
                            map.put(fieldName, DictUtil.getLabel(code, value,dict.splitBy()));
                        }
                    });
        }catch (Exception e){
            DictUtil.LOGGER.error("字典填充器错误",e);
        }
        return map;
    }
}
