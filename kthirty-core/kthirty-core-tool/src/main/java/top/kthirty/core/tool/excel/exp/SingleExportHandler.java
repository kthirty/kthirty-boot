package top.kthirty.core.tool.excel.exp;

import cn.hutool.poi.excel.ExcelWriter;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.util.List;

/**
 * 单Sheet页导出处理器
 * @param <E>
 */
public class SingleExportHandler<E> implements ExportHandler<E> {

    @Override
    public ExcelWriter write(List<E> list, Class<E> clazz, ExcelParams params) {
        // todo 单Sheet页导出逻辑
        throw new RuntimeException("尚未实现");
    }
}
