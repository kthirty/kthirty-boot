package top.kthirty.core.db.listener;

import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import top.kthirty.core.boot.secure.SecureUtil;

import java.util.Date;
/**
 * <p>
 * 数据操作监听，用于填充基础数据
 * </p>
 *
 * @author KThirty
 * @since 2023/11/20
 */
public class OperatingListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object entity) {
        setIfExists(entity, "createBy", SecureUtil.getUsername(),true);
        setIfExists(entity, "orgCode", SecureUtil.getOrgCode(),false);
        setIfExists(entity, "deleted", false,false);
        setIfExists(entity, "createDate", new Date(),false);
        setIfExists(entity, "tenantId", SecureUtil.getTenantId(),false);
    }

    @Override
    public void onUpdate(Object entity) {
        setIfExists(entity, "updateBy", SecureUtil.getUsername(),true);
        setIfExists(entity, "updateDate", new Date(),true);
    }

    /**
     * 如果
     * @param entity 实体
     * @param fieldName 字段名
     * @param value 值
     * @param force 是否强制（不为空覆盖原始值）
     */
    private void setIfExists(Object entity, String fieldName, Object value,boolean force) {
        if (entity != null && ReflectUtil.hasField(entity.getClass(), fieldName)) {
            if(force || ReflectUtil.getFieldValue(entity, fieldName) == null){
                try{
                    ReflectUtil.setFieldValue(entity, fieldName, value);
                }catch (Throwable ignore){}
            }
        }
    }
}
