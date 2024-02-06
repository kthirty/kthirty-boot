package top.kthirty.core.db.base.entity;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class DataEntity<T> extends IdEntity<String> {

    /**
     * 数据创建人
     */
    @Column
    protected String createBy;
    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column
    protected Date createDate;
    /**
     * 最后一次更新人
     */
    @Column
    protected String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, shape = JsonFormat.Shape.STRING)
    @Column
    protected Date updateDate;
}
