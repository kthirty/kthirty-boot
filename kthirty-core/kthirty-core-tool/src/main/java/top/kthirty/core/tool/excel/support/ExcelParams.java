package top.kthirty.core.tool.excel.support;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import top.kthirty.core.tool.excel.handler.CellReader;
import top.kthirty.core.tool.excel.handler.CellStyleEditor;
import top.kthirty.core.tool.excel.handler.CellWriter;

@Data
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

    /**
     * 导入时的字段值修改器
     */
    private CellReader cellReader;
    /**
     * 导出时的字段值修改器
     */
    private CellWriter cellWriter;
    /**
     * 导出字段样式修改器
     */
    private CellStyleEditor cellStyleEditor;

    /**
     * 序号
     */
    private String seqName = "序号";

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
    public void defaultCellReader(CellReader reader){
        if(ObjUtil.isNull(this.cellReader)){
            this.cellReader = reader;
        }
    }
    public void defaultCellWriter(CellWriter writer){
        if(ObjUtil.isNull(this.cellWriter)){
            this.cellWriter = writer;
        }
    }
    public void defaultCellStyleEditor(CellStyleEditor cellStyleEditor){
        if(ObjUtil.isNull(this.cellStyleEditor)){
            this.cellStyleEditor = cellStyleEditor;
        }
    }


}
