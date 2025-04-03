package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.db.sequence.SequenceCode;
import top.kthirty.core.db.sequence.handler.TreeSeqHandler;
import top.kthirty.core.tool.dict.Dict;

import java.util.List;

/**
 * 岗位信息 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
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
    @Dict(code = "enable_status")
    @ColumnDefine(value = ColumnDefine.Type.SHORT_STRING)
    private String status;

    /**
     * 上级岗位ID
     */
    @Schema(description = "上级岗位ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String parentId;

    /**
     * 岗位名称
     */
    @Schema(description = "岗位名称")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String name;

    /**
     * 岗位代码
     */
    @Schema(description = "岗位代码")
    @SequenceCode(handler = TreeSeqHandler.class,rebuildCache = true)
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @ColumnDefine(ColumnDefine.Type.INTEGER)
    private Integer sort;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String description;

    @Column(ignore = true)
    private List<Post> children;

}
