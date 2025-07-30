package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.table.DevFormIndexTableDef;
import top.kthirty.develop.entity.table.DevFormItemTableDef;
import top.kthirty.develop.mapper.DevFormMapper;
import top.kthirty.develop.service.DevFormIndexService;
import top.kthirty.develop.service.DevFormItemService;
import top.kthirty.develop.service.DevFormService;

import java.io.Serializable;

/**
 * form开发 服务层实现。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@Service
@AllArgsConstructor
public class DevFormServiceImpl extends ServiceImpl<DevFormMapper, DevForm> implements DevFormService {
    private final DevFormItemService devFormItemService;
    private final DevFormIndexService devFormIndexService;
    @Override
    public boolean save(DevForm entity) {
        Assert.isTrue(super.save(entity), "保存失败");
        saveDetail(entity);
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id) && removeDetail(id);
    }

    @Override
    public boolean updateById(DevForm entity) {
        Assert.isTrue(super.updateById(entity), "保存失败");
        removeDetail(entity.getId());
        saveDetail(entity);
        return super.updateById(entity);
    }

    private boolean removeDetail(Serializable id) {
        devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(id));
        devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(id));
        return true;
    }

    private void saveDetail(DevForm entity) {
        if(CollUtil.isNotEmpty(entity.getItems())){
            entity.getItems().forEach(it -> it.setFormId(entity.getId()).setId(null));
            devFormItemService.saveBatch(entity.getItems());
        }
        if(CollUtil.isNotEmpty(entity.getIndexes())){
            entity.getIndexes().forEach(it -> it.setFormId(entity.getId()).setId(null));
            devFormIndexService.saveBatch(entity.getIndexes());
        }
    }
}
