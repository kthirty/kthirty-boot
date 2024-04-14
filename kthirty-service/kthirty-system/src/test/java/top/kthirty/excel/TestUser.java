package top.kthirty.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import top.kthirty.core.tool.excel.Excel;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(title = "用户信息")
@Excel(title = "用户信息")
public class TestUser implements Serializable {
    @Excel(title = "姓名")
    private String name;
    @Excel(title = "年龄")
    private Integer age;
    @Excel(title = "账户列表")
    private List<TestAccount> accounts;
    @Excel(title = "地址列表")
    private List<TestAddress> addresses;
}
@Data
@Accessors(chain = true)
class TestAccount{
    @Excel(title = "账户名称")
    private String name;
    @Excel(title = "余额")
    private Double balance;
    @Excel(title = "账户变更记录")
    private List<TestAccountRecord> records;
}
@Data
@Accessors(chain = true)
class TestAddress{
    @Excel(title = "说明")
    private String description;
    @Excel(title = "地址")
    private String location;
}
@Data
@Accessors(chain = true)
class TestAccountRecord{
    @Excel(title = "备注")
    private String remarks;
    @Excel(title = "变更前余额")
    private Double beforeBalance;
    @Excel(title = "变更后余额")
    private Double afterBalance;
}
