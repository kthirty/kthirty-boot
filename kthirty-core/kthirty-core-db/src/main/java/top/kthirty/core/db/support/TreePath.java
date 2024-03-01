package top.kthirty.core.db.support;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryCondition;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.RuleCodeUtil;

public class TreePath {
    public static void setCode(Object obj,String codeField) {
        Assert.notNull(obj);
        Assert.isTrue(ReflectUtil.hasField(obj.getClass(), codeField), "must have code field");
        Assert.isTrue(ReflectUtil.hasField(obj.getClass(), "parentId"), "must have parentId field");
        Table table = AnnotationUtil.getAnnotation(obj.getClass(), Table.class);
        Assert.isTrue(table != null && StrUtil.isNotBlank(table.value()), "@Table not found");
        String parentId = Convert.toStr(ReflectUtil.getFieldValue(obj, "parentId"), "");
        String parentCode = "";
        if (Func.isNotBlank(parentId)) {
            Object one = QueryChain.of(obj.getClass())
                    .select(codeField)
                    .from(table.value())
                    .where( QueryCondition.create(new QueryColumn("id"),parentId))
                    .one();
            if(one != null){
                parentCode = Convert.toStr(ReflectUtil.getFieldValue(one,codeField));
            }
        }
        String code = RuleCodeUtil.getInstance(RuleCodeUtil.HandlerPool.SINGLE_LETTER,
                Func.join(":",table.value(),codeField))
                .next(parentCode);
        ReflectUtil.setFieldValue(obj,codeField,code);
    }

    public static void setCode(Object obj){
        setCode(obj,"code");
    }
}
