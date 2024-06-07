package top.kthirty.core.db.permission;

import com.mybatisflex.core.dialect.OperateType;
import com.mybatisflex.core.query.QueryTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.kthirty.core.boot.secure.SysUser;

import java.util.List;

/***
 * 权限处理器上下文
 * @author Kthirty
 * @since 2024/6/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class DataPermissionContext {
    private String className;
    private String methodName;
    private Object[] parameters;
    private OperateType operateType;
    private List<QueryTable> tables;
    private SysUser currentUser;
}
