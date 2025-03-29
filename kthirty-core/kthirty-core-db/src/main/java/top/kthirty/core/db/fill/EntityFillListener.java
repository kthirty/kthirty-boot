package top.kthirty.core.db.fill;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

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
        if (entity == null) {
            return;
        }
        handle(entity,FillData.Scope.INSERT);
    }

    @Override
    public void onUpdate(Object entity) {
        handle(entity,FillData.Scope.UPDATE);
    }
    private void handle(Object entity, FillData.Scope fillScope) {
        Field[] fields = ReflectUtil.getFields(entity.getClass(), field -> AnnotationUtil.hasAnnotation(field, FillData.class));
        for (Field field : fields) {
            FillData annotation = AnnotationUtil.getAnnotation(field, FillData.class);
            if(annotation.scope() != fillScope && annotation.scope() != FillData.Scope.INSERT_UPDATE){
                return;
            }
            // 不覆盖，且值不为空
            if(!annotation.override() && ObjUtil.isNotNull(ReflectUtil.getFieldValue(entity,field))){
                return;
            }
            FillHandler<?> fillHandler = Singleton.get(annotation.value());
            Object val = fillHandler.getVal(entity, entity.getClass(), field, annotation.args());
            if(val == null){
                return;
            }
            if (ClassUtil.isAssignable(field.getType(),val.getClass())) {
                ReflectUtil.setFieldValue(entity, field, val);
            }
        }
    }
}
