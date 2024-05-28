package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户岗位关联表 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户岗位关联表")
@Table(value = "sys_user_post_rl")
public class UserPostRl extends IdEntity {

    /**
     * 岗位代码
     */
    @Schema(description = "岗位代码")
    private String postCode;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

}
