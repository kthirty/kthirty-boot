package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户部门关联表 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户部门关联表")
@Table(value = "sys_user_dept_rl")
public class UserDeptRl extends IdEntity {

    /**
     * 部门代码
     */
    @Schema(description = "部门代码")
    private String deptCode;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

}
