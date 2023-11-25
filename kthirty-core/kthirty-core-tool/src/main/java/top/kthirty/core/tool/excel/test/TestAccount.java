package top.kthirty.core.tool.excel.test;

import lombok.Data;
import top.kthirty.core.tool.excel.Excel;

@Data
public class TestAccount {
    @Excel(title = "账户类型")
    private String type;
    @Excel(title = "余额")
    private Double balance;
}
