package top.kthirty.core.tool.excel.imp;

import top.kthirty.core.tool.excel.support.ExcelParams;

import java.io.InputStream;
import java.util.List;
/**
 * <p>
 * 单页合并单元格导入处理器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public class SingleImportHandler<E> implements ImportHandler<E>{
    @Override
    public List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        // todo 单Sheet页导入逻辑
        throw new RuntimeException("尚未实现");
    }
}
