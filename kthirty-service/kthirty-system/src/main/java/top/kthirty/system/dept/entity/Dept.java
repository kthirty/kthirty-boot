package top.kthirty.system.dept.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门信息 实体类。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "部门信息")
@Table(value = "sys_dept")
public class Dept extends LogicEntity {

    /**
     * 状态(1/0)
     */
    @Schema(description = "状态(1/0)")
    private String status;

    /**
     * 机构类型10公司，20组织机构，30岗位
     */
    @Schema(description = "机构类型10公司，20组织机构，30岗位")
    private Integer category;

    /**
     * 上级部门ID
     */
    @Schema(description = "上级部门ID")
    private String parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String name;

    /**
     * 部门代码
     */
    @Schema(description = "部门代码")
    private String code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

}
