package top.kthirty.system.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.core.db.support.Query;
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "菜单查询条件")
public class MenuQuery extends Query {
    @Schema(description = "菜单名称")
    private String name;
}
