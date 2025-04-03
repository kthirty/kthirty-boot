package top.kthirty.develop.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.mapper.DevFormMapper;
import top.kthirty.develop.service.DevFormService;

/**
 * form开发 服务层实现。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@Service
public class DevFormServiceImpl extends ServiceImpl<DevFormMapper, DevForm> implements DevFormService {

}
