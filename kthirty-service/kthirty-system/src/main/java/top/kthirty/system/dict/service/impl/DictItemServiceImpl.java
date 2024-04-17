package top.kthirty.system.dict.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.dict.entity.DictItem;
import top.kthirty.system.dict.mapper.DictItemMapper;
import top.kthirty.system.dict.service.DictItemService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Thinkpad
 * @since 2024-04-17
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

}
