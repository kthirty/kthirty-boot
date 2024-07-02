package top.kthirty.develop.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.mapper.DevFormItemMapper;
import top.kthirty.develop.service.DevFormItemService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@Service
public class DevFormItemServiceImpl extends ServiceImpl<DevFormItemMapper, DevFormItem> implements DevFormItemService {

}
