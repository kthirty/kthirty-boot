package top.kthirty.core.tool.excel.imp;

import top.kthirty.core.tool.excel.support.ExcelParams;

import java.io.InputStream;
import java.util.List;

public class SingleImportHandler<E> implements ImportHandler<E>{
    @Override
    public List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        return null;
    }
}
