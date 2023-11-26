package top.kthirty.core.tool.excel.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.kthirty.core.tool.excel.exp.ExportHandler;
import top.kthirty.core.tool.excel.exp.MultipleExportHandler;
import top.kthirty.core.tool.excel.exp.SingleExportHandler;
import top.kthirty.core.tool.excel.imp.ImportHandler;
import top.kthirty.core.tool.excel.imp.MultipleImportHandler;
import top.kthirty.core.tool.excel.imp.SingleImportHandler;

@Getter
@AllArgsConstructor
public enum ExcelStyle {
    /**
     * 所有都在一个Sheet页
     * 存在子类则使用合并单元格方式进行关联
     */
    SINGLE("SINGLE", SingleImportHandler.class, SingleExportHandler.class),
    /**
     * 使用多个Sheet页形式
     * 存在子类则使用多个Sheet页
     * 下一个Sheet页的数据首列应为
     * 序号
     * {主类Sheet页名称_1}
     */
    MULTIPLE("MULTIPLE", MultipleImportHandler.class, MultipleExportHandler.class);

    private final String name;
    private final Class<? extends ImportHandler> importHandlerClass;
    private final Class<? extends ExportHandler> exportHandlerClass;
}
