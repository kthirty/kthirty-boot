package top.kthirty.core.tool.excel.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.cell.CellUtil;
import cn.hutool.poi.excel.reader.BeanSheetReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static top.kthirty.core.tool.excel.support.ExcelHelper.*;

/**
 * 多Sheet页导入处理器
 *
 * @param <E>
 */
public class MultipleImportHandler<E> implements ImportHandler<E> {
    @Override
    public List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        Workbook book = WorkbookUtil.createBook(inputStream);
        String firstSheetName = ExcelHelper.getClassTitle(clazz);
        return deepRead(book, clazz, firstSheetName, params, i -> true);
    }


    public static <E> List<E> deepRead(Workbook workbook, Class<E> clazz, String sheetName, ExcelParams params, Func1<Map, Boolean> filter) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return null;
        }
        ExcelReader reader = new ExcelReader(sheet);
        // 本次需要处理的字段
        List<Field> fields = getFieldsByGroup(clazz, params.getGroups());
        // 计算读取行
        int headerRowIndex = getStartRow(reader, params.getIgnoreStartRow());
        int startRowIndex = headerRowIndex + 1;
        int endRowIndex = reader.getRowCount();
        // 设置Bean读取器
        final BeanSheetReader<Map> beanSheetReader = new BeanSheetReader<>(headerRowIndex, startRowIndex, endRowIndex, Map.class);
        beanSheetReader.setIgnoreEmptyRow(true);
        beanSheetReader.setHeaderAlias(getHeaderAlias(fields));
        if (ObjUtil.isNotNull(params.getCellReader())) {
            params.getCellReader().init((Func1<Cell, Field>) cell -> {
                String title = Func.toStr(CellUtil.getCellValue(reader.getCell(cell.getColumnIndex(), headerRowIndex)));
                return ExcelHelper.getFieldByTitle(clazz,title);
            });
            beanSheetReader.setCellEditor(params.getCellReader());
        }

        // 开始读取
        List<Map> records = reader.read(beanSheetReader);
        // 根据传入Filter去除无效数据
        records = records.stream().filter(filter::callWithRuntimeException).toList();
        // 类型转换
        List<E> result = new ArrayList<>(records.size());
        for (Map item : records) {
            E bean = BeanUtil.toBean(item,clazz,CopyOptions.create().ignoreError().ignoreCase());
            // 处理子类
            Field[] subTableFields = getSubTableFields(clazz, params.getGroups());
            if (Func.isNotEmpty(subTableFields)) {
                for (Field subTableField : subTableFields) {
                    Class<?> subClass = getFieldGenericType(subTableField);
                    String title = getFieldTitle(subTableField);
                    List<?> subResult = deepRead(workbook, subClass, title, params, subItem -> {
                        // 下级的 {sheetName序号} 属性要与当前数据的{序号}属性相同
                        String mainId = MapUtil.getStr(item, params.getSeqName());
                        String subMainId = MapUtil.getStr(subItem, sheetName + params.getSeqName());
                        return Func.equalsSafe(mainId, subMainId);
                    });
                    if (subResult != null) {
                        ReflectUtil.setFieldValue(bean, subTableField, subResult);
                    }
                }
            }
            result.add(bean);
        }
        return result;
    }
}
