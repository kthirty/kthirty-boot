package top.kthirty.core.db.base.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.fill.FillData;
import top.kthirty.core.db.fill.handler.OperatingUserHandler;

import java.util.Date;

/**
 * <p>
 * 基础数据基类
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
public class DataEntity extends IdEntity {
    /**
     * 数据创建人
     */
    @Column(value = "create_by")
    @FillData(value = OperatingUserHandler.class, scope = FillData.Scope.INSERT)
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    protected String createBy;
    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column(value = "create_date",onInsertValue = "now()")
    @ColumnDefine(ColumnDefine.Type.DATETIME)
    protected Date createDate;
    /**
     * 最后一次更新人
     */
    @Column(value = "update_by")
    @FillData(value = OperatingUserHandler.class, scope = FillData.Scope.INSERT_UPDATE)
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    protected String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column(value = "update_date", onInsertValue = "now()",onUpdateValue = "now()")
    @ColumnDefine(ColumnDefine.Type.DATETIME)
    protected Date updateDate;
}
