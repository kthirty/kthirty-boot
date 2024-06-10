package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

import java.sql.Timestamp;

/**
 * 用户信息 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
@Table(value = "sys_user")
public class User extends LogicEntity {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;
    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 生日
     */
    @Schema(description = "生日")
    private Timestamp birthday;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Dict(code = "enable_status")
    private String status;

}
