package top.kthirty.core.tool.excel.exp;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.excel.handler.CellStyleEditor;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 多Sheet页导出处理器
 *
 * @param <E>
 */
@Slf4j
public class MultipleExportHandler<E> implements ExportHandler<E> {

    @Override
    public ExcelWriter write(List<E> list, Class<E> clazz, ExcelParams params) {
        String sheetName = ExcelHelper.getClassTitle(clazz);
        ExcelWriter writer = ExcelUtil.getWriterWithSheet(sheetName);
        deepWrite(writer, list, clazz, sheetName, params, null);
        return writer;
    }

    @Data
    @AllArgsConstructor
    public static class ParentInfo {
        /**
         * 标题，如 用户信息序号
         */
        private String header;
        /**
         * 值，如 用户信息的序号
         */
        private int seq;
    }

    @SuppressWarnings("unchecked")
    public static <E> void deepWrite(ExcelWriter writer, List<E> records, Class<E> clazz, String sheetName, ExcelParams params, ParentInfo parentInfo) {
        ExcelHelper.activeSheet(writer, sheetName);
        List<Field> fields = ExcelHelper.getFieldsByGroup(clazz, params.getGroups());
        List<String> fieldNames = fields.stream().map(Field::getName).toList();
        if (Func.isNotEmpty(fields)) {
            // 是否为首次写入，首次写入需要写入标题
            boolean firstWrite = writer.getSheet().getLastRowNum() == -1;
            if (firstWrite) {
                // 单元格修改器
                List<CellStyleEditor.Param> styleParams = new ArrayList<>();
                List<Object> titles = fields.stream().map(f -> (Object) ExcelHelper.getFieldTitle(f)).toList();
                List<Object> header = getRow(writer, params.getSeqName(), Func.notNull(parentInfo) ? parentInfo.getHeader() : null, titles, styleParams, fields,true);
                writer.writeHeadRow(header);
                // 单元格样式处理器
                if (Func.notNull(params.getCellStyleEditor())) {
                    styleParams.forEach(item -> params.getCellStyleEditor().edit(item));
                }
            }
            for (int i = 0; i < records.size(); i++) {
                ExcelHelper.activeSheet(writer, sheetName);
                // 写入主表数据
                E currentObj = records.get(i);
                int currentSeq = i + 1;
                List<Object> dataRow = new ArrayList<>(fieldNames.stream().map(fieldName -> params.getCellWriter().edit(currentObj, fieldName)).toList());
                // 单元格修改器
                List<CellStyleEditor.Param> styleParams = new ArrayList<>();
                List<Object> row = getRow(writer, currentSeq, Func.notNull(parentInfo) ? parentInfo.getSeq() : null, dataRow, styleParams, fields,false);
                writer.writeRow(row);
                // 单元格样式处理器
                if (Func.notNull(params.getCellStyleEditor())) {
                    styleParams.forEach(item -> params.getCellStyleEditor().edit(item));
                }

                // 写子表数据
                Field[] subTableFields = ExcelHelper.getSubTableFields(clazz, params.getGroups());
                for (Field subTableField : subTableFields) {
                    Class<?> subClass = ExcelHelper.getFieldGenericType(subTableField);
                    String title = ExcelHelper.getFieldTitle(subTableField);
                    Object fieldValue = ReflectUtil.getFieldValue(currentObj, subTableField);
                    if (fieldValue instanceof List) {
                        List subRecords = ListUtil.toList((List) fieldValue);
                        deepWrite(writer, subRecords, subClass, title, params, new ParentInfo(sheetName + params.getSeqName(), currentSeq));
                    }
                }
            }
        }
    }


    private static List<Object> getRow(ExcelWriter writer, Object seq, Object parentSeq, List<Object> values, List<CellStyleEditor.Param> styleParams, List<Field> fields,boolean isHeader) {
        // 处理上级序号
        List<Object> header = new ArrayList<>();
        if (ObjUtil.isNotNull(parentSeq)) {
            header.add(parentSeq);
            styleParams.add(CellStyleEditor.Param.build(writer, 0, null, isHeader, false, true));
        }
        // 处理当前序号
        header.add(seq);
        styleParams.add(CellStyleEditor.Param.build(writer, header.size() - 1, null, isHeader, true, false));
        // 字段
        for (int i = 0; i < values.size(); i++) {
            header.add(values.get(i));
            styleParams.add(CellStyleEditor.Param.build(writer, header.size() - 1, fields.get(i), isHeader, false, false));
        }
        return header;
    }
}
