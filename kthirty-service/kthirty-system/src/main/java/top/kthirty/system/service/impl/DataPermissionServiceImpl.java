package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.system.entity.DataPermission;
import top.kthirty.system.mapper.DataPermissionMapper;
import top.kthirty.system.service.DataPermissionService;

/**
 * 数据权限 服务层实现。
 *
 * @author Thinkpad
 * @since 2024-05-31
 */
@Service
public class DataPermissionServiceImpl extends ServiceImpl<DataPermissionMapper, DataPermission> implements DataPermissionService {

}
