package top.kthirty.system.user.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kthirty.core.db.base.entity.LogicEntity;

import java.sql.Timestamp;

/**
 * 用户信息 实体类。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
@Table(value = "sys_user")
public class User extends LogicEntity<String> {

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
     * 编码
     */
    @Schema(description = "编码")
    private String code;

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
    private String status;

    /**
     * 岗位
     */
    @Schema(description = "岗位")
    private String postCode;

    /**
     * 部门
     */
    @Schema(description = "部门")
    private String deptCode;

    /**
     * 角色代码
     */
    @Schema(description = "角色")
    private String roleCode;

}
