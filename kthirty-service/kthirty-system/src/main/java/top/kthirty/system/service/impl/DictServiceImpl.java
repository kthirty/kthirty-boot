package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.entity.Dict;
import top.kthirty.system.mapper.DictMapper;
import top.kthirty.system.service.DictService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

}
