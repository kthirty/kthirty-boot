package top.kthirty.system.menu.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单 实体类。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜单")
@Table(value = "sys_menu")
public class Menu extends LogicEntity {

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 类型(1菜单,2权限)
     */
    @Schema(description = "类型(1菜单,2权限)")
    private String type;

    /**
     * 菜单CODE
     */
    @Schema(description = "菜单CODE")
    private String code;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String permission;

    /**
     * 上级ID
     */
    @Schema(description = "上级ID")
    private String parentId;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 请求地址
     */
    @Schema(description = "请求地址")
    private String path;

    /**
     * 前端组件
     */
    @Schema(description = "前端组件")
    private String component;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 状态(1启用/0禁用)
     */
    @Schema(description = "状态(1启用/0禁用)")
    private String status;

}
