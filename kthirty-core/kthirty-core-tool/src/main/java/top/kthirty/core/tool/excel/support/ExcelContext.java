package top.kthirty.core.tool.excel.support;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

@Slf4j
@Data
public class ExcelContext {
    private Sheet sheet;
    private int headerStartRow = 0;
    private int headerEndRow = 0;
    private int currentRow = 0;
    private int currentCol = 0;

    public void addRow() {
        ++this.currentRow;
    }

    public void subRow() {
         --this.currentRow;
    }

    public void addCol() {
         ++this.currentCol;
    }

    public void subCol() {
         --this.currentCol;
    }

    public void nextRowStart() {
        this.currentRow++;
        this.currentCol = 0;
    }
    public Row getRow(){
        return sheet.getRow(currentRow);
    }

    public void print() {
        log.info("current {} {}",currentRow,currentCol);
    }
}
