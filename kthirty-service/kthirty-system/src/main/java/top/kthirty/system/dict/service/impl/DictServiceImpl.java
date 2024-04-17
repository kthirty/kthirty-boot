package top.kthirty.system.dict.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.dict.entity.Dict;
import top.kthirty.system.dict.mapper.DictMapper;
import top.kthirty.system.dict.service.DictService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Thinkpad
 * @since 2024-04-17
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

}
