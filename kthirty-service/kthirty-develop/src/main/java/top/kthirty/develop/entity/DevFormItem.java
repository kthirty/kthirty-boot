package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 *  实体类。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
@Table("dev_form_item")
public class DevFormItem extends LogicEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * dev_form表ID
     */
    @Schema(description = "dev_form表ID")
    private String formId;

    /**
     * 表字段名
     */
    @Schema(description = "表字段名")
    private String columnName;

    /**
     * 表字段类型
     */
    @Schema(description = "表字段类型")
    private String columnType;

    /**
     * 表字段长度
     */
    @Schema(description = "表字段长度")
    private Integer columnLength;

    /**
     * 小数点长度
     */
    @Schema(description = "小数点长度")
    private Integer columnPointLength;

    /**
     * 默认值
     */
    @Schema(description = "默认值")
    private String columnDefaultVal;

    /**
     * 是否允许为空
     */
    @Schema(description = "是否允许为空")
    private Integer columnNullable;

    /**
     * 字段备注
     */
    @Schema(description = "字段备注")
    private String columnRemarks;

    /**
     * Java字段类型
     */
    @Schema(description = "Java字段类型")
    private String fieldType;

    /**
     * 字段属性[1普通字段2主键3删除标记4非数据库字段]
     */
    @Schema(description = "字段属性[1普通字段2主键3删除标记4非数据库字段]")
    private String fieldAttribute;

    /**
     * 字典Code
     */
    @Schema(description = "字典Code")
    private String dictCode;

    /**
     * 表字段显示组件
     */
    @Schema(description = "表字段显示组件")
    private String formComponent;

    /**
     * 是否必填
     */
    @Schema(description = "是否必填")
    private Integer formRequired;

    /**
     * 正则验证
     */
    @Schema(description = "正则验证")
    private String formRegexp;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer weight;

    /**
     * 是否查询
     */
    @Schema(description = "是否查询")
    private Integer isShowQuery;

    /**
     * 查询模式
     */
    @Schema(description = "查询模式")
    private String queryComponent;

    /**
     * 是否Form中显示
     */
    @Schema(description = "是否Form中显示")
    private Integer isShowForm;

    /**
     * 是否List中显示
     */
    @Schema(description = "是否List中显示")
    private Integer isShowList;

    /**
     * 是否允许排序
     */
    @Schema(description = "是否允许排序")
    private Integer isAllowSort;

    /**
     * 是否只读
     */
    @Schema(description = "是否只读")
    private Integer isReadonly;

    /**
     * 外键主表名
     */
    @Schema(description = "外键主表名")
    private String foreignKeyMainTable;

    /**
     * 外键主表字段
     */
    @Schema(description = "外键主表字段")
    private String foreignKeyMainColumn;

}
