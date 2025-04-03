package top.kthirty.core.db.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.ColumnDefault;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.fill.FillData;
import top.kthirty.core.db.fill.handler.OperatingUserHandler;
import top.kthirty.core.db.fill.handler.OperatingUserOrgHandler;
import top.kthirty.core.tool.support.Constant;

/**
 * @description 含当前部门编码
 * @author KThirty
 * @since 2025/4/3 9:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrgEntity extends DataEntity{
    @Column(value = "org_code")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    @FillData(value = OperatingUserOrgHandler.class, scope = FillData.Scope.INSERT)
    protected String orgCode;
    @JsonIgnore
    @ColumnDefine(ColumnDefine.Type.BOOLEAN)
    @Column(isLogicDelete = true)
    @ColumnDefault(Constant.NO)
    protected Boolean deleted;
}
