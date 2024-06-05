package top.kthirty.core.db.listener;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.util.StringUtil;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.cache.RuntimeCache;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.utils.RuleCodeUtil;
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
                .forEach(it -> {
                    String next = getNextCode(o, it);
                    ReflectUtil.setFieldValue(o,it,next);
                });
    }

    private static String getNextCode(Object o, Field field) {
        SequenceCode sequenceCode = AnnotationUtil.getAnnotation(field, SequenceCode.class);
        String column = StringUtil.camelToUnderline(field.getName());
        Table table = AnnotationUtil.getAnnotation(o.getClass(), Table.class);
        Kv param = Kv.init().set("column", column).set("table", table.value()).setAll(BeanUtil.toMap(o));
        RuleCodeUtil util = RuleCodeUtil.getInstance(ReflectUtil.newInstance(sequenceCode.handler()), StrUtil.join(":", table.value(), column));
        // 判断本次启动后是否创建过缓存
        String key = StrUtil.join(StringPool.COLON, "RuleCodeCacheBuild",sequenceCode.handler().getName(), table.value(), column);
        if (!RuntimeCache.has(key)) {
            Db.selectAll(table.value()).forEach(item -> util.record(item.getString(column)));
            RuntimeCache.set(key,"1");
        }

        // 判断是否需要拼接前缀
        String prefix = null;
        if(sequenceCode.appendPrefix()){
            String sql = StrUtil.format(sequenceCode.appendPrefixSql(), param);
            Row row = Db.selectOneBySql(sql);
            if(row != null){
                prefix = row.getString("prefix");
            }
        }
        return util.next(prefix);
    }
}
