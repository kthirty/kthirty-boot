package top.kthirty.system.entity;

import cn.hutool.core.convert.Convert;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.db.sequence.SequenceCode;
import top.kthirty.core.db.sequence.handler.NumberSeqHandler;
import top.kthirty.core.tool.dict.Dict;
import top.kthirty.core.tool.jackson.generate.GenerateField;
import top.kthirty.core.tool.utils.StringPool;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
     * 编码
     */
    @Schema(description = "编码")
    @SequenceCode(handler = NumberSeqHandler.class, handlerParams = "6", rebuildCache = true)
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
    @Dict(code = "enable_status")
    private String status;

    @Column(ignore = true)
    @RelationManyToMany(
            joinTable = "sys_user_role_rl",
            joinSelfColumn = "user_id",
            joinTargetColumn = "role_id"
    )
    @GenerateField(genField = "roleName",objField = "name")
    private List<Role> roleList;

    @Column(ignore = true)
    @RelationOneToMany(targetField = "userId")
    private List<UserPosition> userPositions;


    public String getPositionName(){
        if(userPositions == null){
            return null;
        }
        return userPositions.stream().map(it ->
                Convert.toStr(it.__getJsonField("deptIdLabel"))
                + Convert.toStr(it.__getJsonField("postIdLabel")))
                .collect(Collectors.joining(StringPool.COMMA));
    }
}
