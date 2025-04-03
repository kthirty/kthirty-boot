package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.system.entity.DictItem;
import top.kthirty.system.mapper.DictItemMapper;
import top.kthirty.system.service.DictItemService;

/**
 *  服务层实现。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

}
