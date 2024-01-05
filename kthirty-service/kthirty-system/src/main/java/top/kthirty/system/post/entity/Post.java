package top.kthirty.system.post.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位信息 实体类。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "岗位信息")
@Table(value = "sys_post")
public class Post extends LogicEntity {

    /**
     * 状态(1/0)
     */
    @Schema(description = "状态(1/0)")
    private String status;

    /**
     * 上级部门ID
     */
    @Schema(description = "上级部门ID")
    private String parentId;

    /**
     * 岗位名称
     */
    @Schema(description = "岗位名称")
    private String name;

    /**
     * 岗位代码
     */
    @Schema(description = "岗位代码")
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
