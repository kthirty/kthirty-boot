package top.kthirty.core.tool.excel.support;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelParams {
    /**
     * 分组
     */
    private String[] groups;
    /**
     * 样式
     */
    private ExcelStyle style;
    /**
     * 忽略开始的几行（一般为标题）
     */
    private int ignoreStartRow = 0;

    public void defaultGroups(String... groups) {
        if (ArrayUtil.isEmpty(this.groups)) {
            this.groups = groups;
        }
    }
    public void defaultStyle(ExcelStyle style) {
        if (ObjUtil.isNull(this.style)) {
            this.style = style;
        }
    }

}
