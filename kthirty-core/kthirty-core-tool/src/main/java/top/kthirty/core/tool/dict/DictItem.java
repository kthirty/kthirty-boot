package top.kthirty.core.tool.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 数据字典选项值
 * </p>
 *
 * @author KThirty
 * @since 2023/11/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DictItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "ID")
    private String id;
    @Schema(title = "父ID")
    private String parentId;
    @Schema(title = "属性值")
    private String value;
    @Schema(title = "显示值")
    private String label;
    @Schema(title = "权重")
    private int weight;
    @Schema(title = "是否禁用")
    private boolean disabled;
    @Schema(title = "拓展信息")
    private Dict extra;
    @Schema(title = "子选项")
    private List<DictItem> children;
}
