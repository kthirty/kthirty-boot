package top.kthirty.core.db.base.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class LogicEntity<T> extends IdEntity<T>{

    /**
     * 数据创建人
     */
    @Column(value = "create_by")
    private String createBy;
    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column(value = "create_date",onInsertValue = "now()")
    private Date createTime;
    /**
     * 最后一次更新人
     */
    @Column(value = "update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column (value = "update_date", onInsertValue = "now()",onUpdateValue = "now()")
    private Date updateTime;
    /**
     * 删除标记
     */
    @Column(isLogicDelete = true)
    private Boolean deleted;
}
