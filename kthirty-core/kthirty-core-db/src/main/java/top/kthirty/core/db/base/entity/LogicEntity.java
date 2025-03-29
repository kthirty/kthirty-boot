package top.kthirty.core.db.base.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.kthirty.core.db.fill.FillData;
import top.kthirty.core.db.fill.handler.OperatingUserHandler;

import java.util.Date;
/**
 * <p>
 * 逻辑删除实体类
 * </p>
 *
 * @author KThirty
 * @since 2025/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class LogicEntity extends IdEntity{

    /**
     * 数据创建人
     */
    @Column(value = "create_by")
    @FillData(value = OperatingUserHandler.class, scope = FillData.Scope.INSERT)
    protected String createBy;
    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column(value = "create_date",onInsertValue = "now()")
    protected Date createDate;
    /**
     * 最后一次更新人
     */
    @Column(value = "update_by")
    @FillData(value = OperatingUserHandler.class, scope = FillData.Scope.INSERT_UPDATE)
    protected String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column (value = "update_date", onInsertValue = "now()",onUpdateValue = "now()")
    protected Date updateDate;
    /**
     * 删除标记
     */
    @Column(isLogicDelete = true)
    @JsonIgnore
    protected Boolean deleted;
}
