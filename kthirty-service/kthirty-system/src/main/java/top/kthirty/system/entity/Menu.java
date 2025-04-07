package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

import java.util.List;
import java.util.Map;

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
    @ColumnDefine(ColumnDefine.Type.STRING)
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 类型(1菜单,2权限)
     */
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    @Schema(description = "类型(0目录,1菜单,2权限)")
    @Dict(code = "menu_type")
    private String type;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String permission;

    /**
     * 上级ID
     */
    @Schema(description = "上级ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String parentId;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @ColumnDefine(ColumnDefine.Type.INTEGER)
    private Integer sort;

    /**
     * 请求地址
     */
    @Schema(description = "请求地址")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String path;

    /**
     * 前端组件
     */
    @Schema(description = "前端组件")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String component;
    /**
     * 前端组件名
     */
    @Schema(description = "前端组件名称")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String componentName;

    /**
     * 图标
     */
    @Schema(description = "图标")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String icon;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String description;

    /**
     * 状态(1启用/0禁用)
     */
    @Schema(description = "状态(1启用/0禁用)")
    @Dict(code = "enable_status")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String status;
    /**
     * 是否显示(1显示/0隐藏)
     */
    @Schema(description = "状态(1显示/0隐藏)")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    @Dict(code = "show_status")
    private String show;

    @Column(ignore = true)
    private List<Menu> children;

    @Schema(description = "额外参数")
    @ColumnDefine(ColumnDefine.Type.JSON)
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> meta;
}
