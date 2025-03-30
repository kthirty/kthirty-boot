package top.kthirty.core.db.fill;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;

/**
 * <p>
 * 填充监听器
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
public class EntityFillListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object entity) {
        handle(entity,FillData.Scope.INSERT);
    }

    @Override
    public void onUpdate(Object entity) {
        handle(entity,FillData.Scope.UPDATE);
    }
    private void handle(Object entity, FillData.Scope fillScope) {
        if (entity == null) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(entity.getClass());
        for (Field field : fields) {
            FillData annotation = AnnotatedElementUtils.findMergedAnnotation(field, FillData.class);
            if (annotation == null){
                continue;
            }
            if(annotation.scope() != fillScope && annotation.scope() != FillData.Scope.INSERT_UPDATE){
                continue;
            }
            // 不覆盖，且值不为空
            if(!annotation.override() && ObjUtil.isNotNull(ReflectUtil.getFieldValue(entity,field))){
                continue;
            }
            FillHandler<?> fillHandler = Singleton.get(annotation.value());
            Object val = fillHandler.getVal(entity, entity.getClass(), field, annotation.args());
            if(val == null){
                return;
            }
            ReflectUtil.setFieldValue(entity, field, Convert.convert(val.getClass(),val));
        }
    }
}
