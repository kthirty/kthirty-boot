package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

/**
 * 菜单 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
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
    @Schema(description = "类型(0目录,1菜单,2权限)")
    @Dict(code = "menu_type")
    private String type;

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
     * 前端组件名
     */
    @Schema(description = "前端组件名称")
    private String componentName;

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
    @Dict(code = "enable_status")
    private String status;
    /**
     * 是否显示(1显示/0隐藏)
     */
    @Schema(description = "状态(1显示/0隐藏)")
    @Dict(code = "show_status")
    private String show;

}
