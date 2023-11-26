package top.kthirty.core.tool.excel.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import org.apache.poi.ss.usermodel.*;
import top.kthirty.core.tool.excel.Excel;

public class DefaultCellStyleEditor implements CellStyleEditor{
    @Override
    public void edit(Param param) {
        Cell cell = param.getSheet().getRow(param.getRow()).getCell(param.getCell());
        Excel excel = AnnotationUtil.getAnnotation(param.getField(), Excel.class);
        // 设置单元格宽度
        if(excel != null){
            // 256是POI中表示字符宽度的单位
            int width = excel.width() * 256;
            param.getSheet().setColumnWidth(param.getCell(),width);
        }
        // 设置单元格样式
        CellStyle cellStyle = param.getSheet().getWorkbook().createCellStyle();
        // 序号 || 父序号 || 标题 设置字体加粗
        if(param.isSeq() || param.isParentSeq()){
            Font font = param.getSheet().getWorkbook().createFont();
            font.setBold(true);
            cellStyle.setFont(font);
        }
        if(param.isHeader()){
            cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cell.setCellStyle(cellStyle);
    }
}
