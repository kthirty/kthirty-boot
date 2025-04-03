package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.db.fill.FillData;
import top.kthirty.core.db.fill.handler.NumberSeqHandler;
import top.kthirty.core.tool.dict.Dict;
import top.kthirty.core.tool.jackson.generate.GenerateField;

import java.sql.Timestamp;
import java.util.List;

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
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String username;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String realName;

    /**
     * 编码
     */
    @Schema(description = "编码")
    @FillData(value = NumberSeqHandler.class,override = true,scope = FillData.Scope.INSERT)
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String code;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String phone;

    /**
     * 生日
     */
    @Schema(description = "生日")
    @ColumnDefine(ColumnDefine.Type.DATETIME)
    private Timestamp birthday;

    /**
     * 性别
     */
    @Schema(description = "性别")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    @Dict(code = "sex")
    private String sex;

    /**
     * 头像
     */
    @Schema(description = "头像")
    @ColumnDefine(ColumnDefine.Type.LONG_STRING)
    private String avatar;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Dict(code = "enable_status")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String status;

    @RelationManyToMany(
            joinTable = "sys_user_role_rl",
            joinSelfColumn = "user_id",
            joinTargetColumn = "role_id"
    )
    @GenerateField(genField = "roleName",objField = "name")
    private List<Role> roleList;

    @RelationManyToMany(
            joinTable = "sys_user_dept_rl",
            joinSelfColumn = "user_id",
            joinTargetColumn = "dept_id"
    )
    @GenerateField(genField = "deptName",objField = "name")
    private List<Dept> deptList;

    @Column(ignore = true)
    private List<String> roleIds;
    @Column(ignore = true)
    private List<String> deptIds;

    public void setRoleList(List<Role> list){
        if(list == null){
            return;
        }
        this.roleList = list;
        this.roleIds = list.stream().map(IdEntity::getId).toList();
    }

    public void setDeptList(List<Dept> list){
        if(list == null){
            return;
        }
        this.deptList = list;
        this.deptIds = list.stream().map(IdEntity::getId).toList();
    }

}
