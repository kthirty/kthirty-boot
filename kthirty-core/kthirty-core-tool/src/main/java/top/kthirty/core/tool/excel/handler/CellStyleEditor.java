package top.kthirty.core.tool.excel.handler;

import cn.hutool.poi.excel.ExcelWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

/**
 * <p>
 * 单元格写入样式修改
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
public interface CellStyleEditor {
    void edit(Param param);

    CellStyle getStyle(Param param);


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class Param {
        private Sheet sheet;
        private int row;
        private int cell;
        private Field field;
        private boolean isHeader;
        private boolean isSeq;
        private boolean isParentSeq;

        public static Param build(ExcelWriter writer, int cell, Field field, boolean isHeader, boolean isSeq, boolean isParentSeq){
            Param param = new Param();
            param.setSheet(writer.getSheet());
            param.setRow(writer.getCurrentRow());
            param.setCell(cell);
            param.setSeq(isSeq);
            param.setParentSeq(isParentSeq);
            param.setHeader(isHeader);
            param.setField(field);
            return param;
        }
    }

}

