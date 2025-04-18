package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.IdEntity;

/**
 * 用户部门关联表 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户部门关联表")
@Table(value = "sys_user_dept_rl")
public class UserDeptRl extends IdEntity {

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String deptId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String userId;

    @Schema(description = "岗位ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String postId;

    @Schema(description = "是否主部门")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String isMain;
}
