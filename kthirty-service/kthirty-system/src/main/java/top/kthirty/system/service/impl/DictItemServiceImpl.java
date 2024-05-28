package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.entity.DictItem;
import top.kthirty.system.mapper.DictItemMapper;
import top.kthirty.system.service.DictItemService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

}
