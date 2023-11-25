package top.kthirty.core.tool.excel.imp;

import cn.hutool.poi.excel.WorkbookUtil;
import org.apache.poi.ss.usermodel.Workbook;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.io.InputStream;
import java.util.List;

public class MultipleImportHandler<E> implements ImportHandler<E>{
    @Override
    public List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        Workbook book = WorkbookUtil.createBook(inputStream);
        String firstSheetName = book.getSheetName(0);
        return ExcelHelper.deepRead(book,clazz,firstSheetName,params,i -> true);
    }
}
