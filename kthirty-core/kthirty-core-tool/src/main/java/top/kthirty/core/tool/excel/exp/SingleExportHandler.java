package top.kthirty.core.tool.excel.exp;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import top.kthirty.core.tool.excel.handler.CellStyleEditor;
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
    private final Context context = new Context();

    @Override
    public ExcelWriter write(List<E> list, Class<E> clazz, ExcelParams params) {
        this.params = params;
        ExcelWriter writer = new ExcelWriter(true);
        // 标题
        context.headerStartRow = context.currentRow;
        writeTitle(writer, clazz);
        context.headerEndRow = writer.getRowCount() - 1;
        ExcelHelper.mergeToBottom(writer, context.headerStartRow, context.headerEndRow);
        // 开始写内容
        list.forEach(it -> {
            // 切换到最后一行
            context.currentRow = writer.getRowCount();
            context.currentCol = 0;
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
            writer.writeCellValue(context.currentCol, context.currentRow, fieldValue);
            params.getCellStyleEditor().edit(CellStyleEditor.Param.builder().sheet(writer.getSheet()).row(context.currentRow).cell(context.currentCol).isHeader(false).field(field).build());
            context.addCol();
        });
        int currentRow = context.currentRow;
        Arrays.stream(ExcelHelper.getSubTableFields(clazz, params.getGroups())).forEach(field -> {
            context.currentRow = currentRow;
            context.currentCol = writer.getSheet().getRow(currentRow).getLastCellNum();
            Collection<?> fieldValue = (Collection<?>) ReflectUtil.getFieldValue(obj, field);
            Class<?> genericType = ExcelHelper.getFieldGenericType(field);
            fieldValue.forEach(subItem -> {
                int currentCell = context.currentCol;
                writeContent(writer, genericType, subItem);
                context.addRow();
                context.currentCol = currentCell;
            });
            context.subRow();
        });
    }

    private void writeTitle(ExcelWriter writer, Class<?> clazz) {
        // 仅读取非Collection
        List<Field> fields = ExcelHelper.getFieldsByGroup(clazz, params.getGroups());
        for (Field field : fields) {
            String title = ExcelHelper.getFieldTitle(field);
            writer.writeCellValue(context.currentCol, context.currentRow, title);
            params.getCellStyleEditor().edit(CellStyleEditor.Param.builder().sheet(writer.getSheet()).row(context.currentRow).cell(context.currentCol).isHeader(true).field(field).build());
            context.addCol();
        }
        // 读取Collection
        List<Field> subFields = Arrays.stream(ExcelHelper.getSubTableFields(clazz, params.getGroups())).toList();
        for (Field field : subFields) {
            // 切换到下一行开始书写子标题
            context.addRow();
            int startCol = context.currentCol;
            writeTitle(writer, ExcelHelper.getFieldGenericType(field));
            context.subRow();
            // 合并单元格
            String title = ExcelHelper.getFieldTitle(field);
            context.subCol();
            CellStyle style = params.getCellStyleEditor().getStyle(CellStyleEditor.Param.builder().sheet(writer.getSheet()).row(context.currentRow).cell(context.currentCol).isHeader(true).field(field).build());
            writer.merge(context.currentRow, context.currentRow, startCol, context.currentCol, title, style);
            context.addCol();
        }
    }
}

class Context {
    int headerStartRow = 0;
    int headerEndRow = 0;
    int currentRow = 0;
    int currentCol = 0;

    public int addRow() {
        return ++this.currentRow;
    }

    public int subRow() {
        return --this.currentRow;
    }

    public int addCol() {
        return ++this.currentCol;
    }

    public int subCol() {
        return --this.currentCol;
    }

    public void nextRowStart() {
        this.currentRow++;
        this.currentCol = 0;
    }
}
