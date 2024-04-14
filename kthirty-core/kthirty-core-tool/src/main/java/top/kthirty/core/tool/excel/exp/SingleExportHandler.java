package top.kthirty.core.tool.excel.exp;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import top.kthirty.core.tool.excel.handler.CellStyleEditor;
import top.kthirty.core.tool.excel.support.ExcelContext;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 单Sheet页导出处理器
 *
 * @param <E>
 */
@Slf4j
public class SingleExportHandler<E> implements ExportHandler<E> {
    private ExcelParams params;
    private final ExcelContext context = new ExcelContext();

    @Override
    public ExcelWriter write(List<E> list, Class<E> clazz, ExcelParams params) {
        this.params = params;
        ExcelWriter writer = new ExcelWriter(true);
        // 标题
        context.setHeaderStartRow(context.getCurrentRow());
        writeTitle(writer, clazz);
        context.setHeaderStartRow(writer.getRowCount() - 1);
        ExcelHelper.mergeToBottom(writer, context.getHeaderStartRow(), context.getHeaderEndRow());
        // 开始写内容
        list.forEach(it -> {
            // 切换到最后一行
            context.setCurrentRow(writer.getRowCount());
            context.setCurrentCol(0);
            writeContent(writer, clazz, it);
        });

        return writer;
    }

    private void writeContent(ExcelWriter writer, Class<?> clazz, Object obj) {
        // 写基础字段
        ExcelHelper.getFieldsByGroup(clazz, params.getGroups()).forEach(field -> {
            Object fieldValue = ReflectUtil.getFieldValue(obj, field);
            if (params.getCellWriter() != null) {
                fieldValue = params.getCellWriter().edit(obj, field.getName());
            }
            writer.writeCellValue(context.getCurrentCol(), context.getCurrentRow(), fieldValue);
            params.getCellStyleEditor().edit(CellStyleEditor.Param.builder()
                    .sheet(writer.getSheet())
                    .row(context.getCurrentRow())
                    .cell(context.getCurrentCol())
                    .isHeader(false).field(field).build());
            context.addCol();
        });
        int currentRow = context.getCurrentRow();
        Arrays.stream(ExcelHelper.getSubTableFields(clazz, params.getGroups())).forEach(field -> {
            context.setCurrentRow(currentRow);
            context.setCurrentCol(writer.getSheet().getRow(currentRow).getLastCellNum());
            Collection<?> fieldValue = (Collection<?>) ReflectUtil.getFieldValue(obj, field);
            Class<?> genericType = ExcelHelper.getFieldGenericType(field);
            fieldValue.forEach(subItem -> {
                int currentCell = context.getCurrentCol();
                writeContent(writer, genericType, subItem);
                context.addRow();
                context.setCurrentCol(currentCell);
            });
            context.subRow();
        });
    }

    private void writeTitle(ExcelWriter writer, Class<?> clazz) {
        // 仅读取非Collection
        List<Field> fields = ExcelHelper.getFieldsByGroup(clazz, params.getGroups());
        for (Field field : fields) {
            String title = ExcelHelper.getFieldTitle(field);
            writer.writeCellValue(context.getCurrentCol(), context.getCurrentRow(), title);
            params.getCellStyleEditor().edit(CellStyleEditor.Param.builder()
                    .sheet(writer.getSheet())
                    .row(context.getCurrentRow())
                    .cell(context.getCurrentCol())
                    .isHeader(true).field(field).build());
            context.addCol();
        }
        // 读取Collection
        List<Field> subFields = Arrays.stream(ExcelHelper.getSubTableFields(clazz, params.getGroups())).toList();
        for (Field field : subFields) {
            // 切换到下一行开始书写子标题
            context.addRow();
            int startCol = context.getCurrentCol();
            writeTitle(writer, ExcelHelper.getFieldGenericType(field));
            context.subRow();
            // 合并单元格
            String title = ExcelHelper.getFieldTitle(field);
            context.subCol();
            CellStyle style = params.getCellStyleEditor().getStyle(CellStyleEditor.Param.builder()
                    .sheet(writer.getSheet())
                    .row(context.getCurrentRow())
                    .cell(context.getCurrentCol())
                    .isHeader(true).field(field).build());
            writer.merge(context.getCurrentRow(), context.getCurrentRow(), startCol, context.getCurrentCol(), title, style);
            context.addCol();
        }
    }
}
