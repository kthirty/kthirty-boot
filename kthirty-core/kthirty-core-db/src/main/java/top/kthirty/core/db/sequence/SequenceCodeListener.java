package top.kthirty.core.db.sequence;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.util.StringUtil;
import top.kthirty.core.db.sequence.handler.RuleContext;
import top.kthirty.core.db.sequence.handler.RuleHandler;
import top.kthirty.core.tool.cache.RuntimeCache;
import top.kthirty.core.tool.utils.StringPool;

import java.lang.reflect.Field;
import java.util.Arrays;
/**
 * @description 序列化编码填充器
 * @author KThirty
 * @since 2024/6/5 15:10
 */
public class SequenceCodeListener implements InsertListener {
    @Override
    public void onInsert(Object o) {
        Arrays.stream(ReflectUtil.getFields(o.getClass()
                        , it -> AnnotationUtil.hasAnnotation(it, SequenceCode.class)
                                && it.getType() == String.class))
                .forEach(it -> getNextCode(o, it));
    }

    private static void getNextCode(Object o, Field field) {
        SequenceCode sequenceCode = AnnotationUtil.getAnnotation(field, SequenceCode.class);
        String column = StringUtil.camelToUnderline(field.getName());
        Table table = AnnotationUtil.getAnnotation(o.getClass(), Table.class);
        // 组件规则上下文
        RuleContext ruleContext = new RuleContext(table.value(), column, o);
        RuleHandler ruleHandler = ReflectUtil.newInstance(sequenceCode.handler());
        ruleHandler.init(ruleContext, sequenceCode.handlerParams());
        // 重建索引
        String rebuildCacheKey = StrUtil.join(StringPool.COLON
                , "RuleCodeCacheBuild"
                , ClassUtil.getClassName(sequenceCode.handler(),true)
                , table.value()
                , column);
        if(sequenceCode.rebuildCache() && !RuntimeCache.has(rebuildCacheKey)){
            Db.selectAll(table.value()).forEach(item -> ruleHandler.record(item.getString(column)));
            RuntimeCache.set(rebuildCacheKey,"1");
        }
        ReflectUtil.setFieldValue(o,field,ruleHandler.next());
    }
}
