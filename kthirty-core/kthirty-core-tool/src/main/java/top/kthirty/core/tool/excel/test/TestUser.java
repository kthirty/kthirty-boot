package top.kthirty.core.tool.excel.test;

import lombok.Data;
import top.kthirty.core.tool.excel.Excel;

import java.util.List;

@Data
@Excel(title = "用户信息")
public class TestUser {
    @Excel(title = "登录名",sort = 10)
    private String username;
    @Excel(title = "姓名",sort = -1)
    private String realName;
    @Excel(title = "年龄")
    private Integer age;
    @Excel(title = "账户信息")
    private List<TestAccount> accounts;
}

