package top.kthirty.system.entity;

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
 * 数据权限 实体类。
 *
 * @author Thinkpad
 * @since 2024-05-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据权限")
@Table("sys_data_permission")
public class DataPermission extends LogicEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private String menuId;

    /**
     * 限制类型(删除限制/修改限制/查询限制)
     */
    @Schema(description = "限制类型(删除限制/修改限制/查询限制)")
    private String type;

    /**
     * 限制规则(预设规则/自定义规则)
     */
    @Schema(description = "限制规则(预设规则/自定义规则)")
    private String ruleType;

    /**
     * 限制规则详情
     */
    @Schema(description = "限制规则详情")
    private String ruleDetail;

    /**
     * Mapper方法全路径
     */
    @Schema(description = "Mapper方法全路径")
    private String mapperMethod;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;

}
