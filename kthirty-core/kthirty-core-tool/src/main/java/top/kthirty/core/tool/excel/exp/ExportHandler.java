package top.kthirty.core.tool.excel.exp;

import cn.hutool.poi.excel.ExcelWriter;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.util.List;

public interface ExportHandler<E> {
    ExcelWriter write(List<E> list, Class<E> clazz, ExcelParams params);
}
