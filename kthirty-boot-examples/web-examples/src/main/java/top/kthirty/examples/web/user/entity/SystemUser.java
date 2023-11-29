package top.kthirty.examples.web.user.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.tool.dict.Dict;

@EqualsAndHashCode(callSuper = true)
@Data
@Table("test_user")
public class SystemUser extends IdEntity<String> {
    @Schema(title = "用户名")
    private String username;

    @Schema(title = "真实姓名")
    private String realName;

    @Dict
    @Schema(title = "性别")
    private String sex;

    @Dict(code = "user_status")
    @Schema(title = "状态")
    private String status;
}
