package top.kthirty.core.tool.jackson.filler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * @description Json字段填充
 * @author KThirty
 * @since 2024/6/14 16:18
 */
public interface JsonFiller {
    /**
     * 使用JsonAnyGetter 给实体添加json字段
     * 自动扫描包含JsonGetter的方法无参方法并集合起来返回jackson
     * @author KThirty
     * @since 2024/6/14 16:19
     */
    @JsonAnyGetter
    default Map<String, Object> __JsonFiller() {
        Map<String,Object> res = new HashMap<>();
        Arrays.stream(ReflectUtil.getMethods(this.getClass(),it -> AnnotationUtil.hasAnnotation(it, JsonGetter.class)
                && it.getParameterCount() == 0
                && it.getReturnType() == Map.class))
                .forEach(it -> res.putAll(ReflectUtil.invoke(this,it)));
        return res;
    }
}
