package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.autotable.annotation.Ignore;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

import java.io.Serial;
import java.util.List;

/**
 * 报表开发 实体类。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表开发")
@Table("dev_report")
public class DevReport extends LogicEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报表编码
     */
    @Schema(description = "报表编码")
    private String code;

    /**
     * 报表名称
     */
    @Schema(description = "报表名称")
    private String name;

    /**
     * 报表SQL
     */
    @Schema(description = "报表SQL")
    private String reportSql;

    /**
     * 是否分页
     */
    @Schema(description = "是否分页")
    @Dict(code = "whether")
    private String isPage;

    /**
     * 数据源Key（对应 Properties 配置 kthirty.develop.datasources.{key}）
     */
    @Schema(description = "数据源Key")
    private String dsKey;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;

    @Ignore
    @Schema(description = "报表字段")
    @Singular
    private List<DevReportItem> items;

}
