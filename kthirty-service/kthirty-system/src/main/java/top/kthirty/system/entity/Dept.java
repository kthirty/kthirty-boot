package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.autotable.annotation.AutoTable;
import top.kthirty.core.db.auto.KtColumn;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.db.sequence.SequenceCode;
import top.kthirty.core.db.sequence.handler.TreeSeqHandler;
import top.kthirty.core.tool.dict.Dict;

import java.util.List;

/**
 * 部门信息 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "部门信息")
@Table(value = "sys_dept")
@AutoTable
public class Dept extends LogicEntity {

    /**
     * 状态(1/0)
     */
    @Schema(description = "状态(1/0)")
    @Dict(code = "enable_status")
    @KtColumn(KtColumn.Type.SHORT_STRING)
    private String status;

    /**
     * 机构类型10公司，20组织机构，30岗位
     */
    @Schema(description = "机构类型10公司，20组织机构，30岗位")
    @Dict(code = "dept_category")
    @KtColumn(KtColumn.Type.SHORT_STRING)
    private String category;

    /**
     * 上级部门ID
     */
    @Schema(description = "上级部门ID")
    @Dict(code = "sys_dept:id:name")
    @KtColumn(KtColumn.Type.SHORT_STRING)
    private String parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @ColumnDefine(type = "varchar", length = 200)
    @KtColumn(KtColumn.Type.STRING)
    private String name;

    /**
     * 部门代码
     */
    @Schema(description = "部门代码")
    @SequenceCode(handler = TreeSeqHandler.class,rebuildCache = true)
    @KtColumn(KtColumn.Type.SHORT_STRING)
    private String code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @KtColumn(KtColumn.Type.INTEGER)
    private Integer sort;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(type = "varchar", length = 255)
    @KtColumn(KtColumn.Type.STRING)
    private String description;

    @Column(ignore = true)
    private List<Dept> children;

}
