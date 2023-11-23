package top.kthirty.core.tool.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class DictItem {
    @Schema(title = "属性值")
    private String value;
    @Schema(title = "显示值")
    private String label;
    @Schema(title = "备注")
    private String remarks;
    @Schema(title = "权重(从小到大)")
    private int weight = 0;
    @Schema(title = "是否禁用")
    private boolean disabled;
    @Schema(title = "字典Code")
    private String code;
}
