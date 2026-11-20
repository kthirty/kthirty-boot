package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.tool.dict.Dict;

import java.io.Serial;

/**
 * 报表开发字段 实体类。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表开发字段")
@Table("dev_report_item")
@Accessors(chain = true)
public class DevReportItem extends IdEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * dev_report表ID
     */
    @Schema(description = "dev_report表ID")
    private String reportId;

    /**
     * 字段名
     */
    @Schema(description = "字段名")
    private String columnName;

    /**
     * 字段备注
     */
    @Schema(description = "字段备注")
    private String columnRemarks;

    /**
     * 字段类型
     */
    @Schema(description = "字段类型")
    private String fieldType;

    /**
     * 字典Code
     */
    @Schema(description = "字典Code")
    private String dictCode;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Double weight;

    /**
     * 是否搜索
     */
    @Schema(description = "是否搜索")
    @Dict(code = "whether")
    private String isShowQuery;

    /**
     * 搜索组件
     */
    @Schema(description = "搜索组件")
    private String queryComponent;

    /**
     * 是否List中显示
     */
    @Schema(description = "是否List中显示")
    @Dict(code = "whether")
    private String isShowList;

    /**
     * 是否允许排序
     */
    @Schema(description = "是否允许排序")
    @Dict(code = "whether")
    private String isAllowSort;

    /**
     * 列表显示组件
     */
    @Schema(description = "列表显示组件")
    private String listComponent;

}
