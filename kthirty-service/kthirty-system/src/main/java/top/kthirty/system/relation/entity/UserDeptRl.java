package top.kthirty.system.relation.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户部门关联表 实体类。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
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
