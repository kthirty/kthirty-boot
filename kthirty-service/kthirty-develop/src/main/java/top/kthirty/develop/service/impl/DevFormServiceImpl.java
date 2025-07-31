package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormIndex;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.entity.table.DevFormIndexTableDef;
import top.kthirty.develop.entity.table.DevFormItemTableDef;
import top.kthirty.develop.mapper.DevFormMapper;
import top.kthirty.develop.service.DevFormIndexService;
import top.kthirty.develop.service.DevFormItemService;
import top.kthirty.develop.service.DevFormService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        entity.setIsDbSync(Constant.NO);
        Assert.isTrue(super.save(entity), "保存失败");
        if (CollUtil.isNotEmpty(entity.getItems())) {
            entity.getItems().forEach(it ->
                    it.setFormId(entity.getId())
                            .setIsDbSync(false)
                            .setId(null));
            devFormItemService.saveBatch(entity.getItems());
        }
        if (CollUtil.isNotEmpty(entity.getIndexes())) {
            entity.getIndexes().forEach(it ->
                    it.setFormId(entity.getId())
                            .setIsDbSync(false)
                            .setId(null));
            devFormIndexService.saveBatch(entity.getIndexes());
        }
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(id));
        devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(id));
        return super.removeById(id);
    }

    @Override
    public boolean updateById(DevForm entity) {
        boolean dbChange = false;
        // 删除库中历史Items
        List<String> itemIds = entity.getItems().stream().map(IdEntity::getId).filter(Objects::isNull).toList();
        boolean removeItems = devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(entity.getId())
                .and(DevFormItemTableDef.DEV_FORM_ITEM.ID.notIn(itemIds)));
        if(removeItems){
            dbChange = true;
        }
        // 删除库中历史Indexes
        List<String> indexIds = entity.getIndexes().stream().map(IdEntity::getId).filter(Objects::isNull).toList();
        boolean removeIndexes = devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(entity.getId())
                .and(DevFormIndexTableDef.DEV_FORM_INDEX.ID.notIn(indexIds)));
        if(removeIndexes){
            dbChange = true;
        }
        // 与库中数据对比，判断是否有变更
        Map<String, DevFormItem> itemMap = devFormItemService.list(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(entity.getId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity(), (a, b) -> a));
        for (DevFormItem it : entity.getItems()) {
            if(it.getId() == null || !itemMap.containsKey(it.getId())){
                it.setIsDbSync(false);
                it.setId(null);
            }else{
                DevFormItem dbInfo = itemMap.get(it.getId());
                if(!Objects.equals(it.getColumnName(),dbInfo.getColumnName()) ||
                        !Objects.equals(it.getColumnLength(),dbInfo.getColumnLength()) ||
                        !Objects.equals(it.getColumnPointLength(),dbInfo.getColumnPointLength()) ||
                        !Objects.equals(it.getColumnType(),dbInfo.getColumnType())){
                    it.setIsDbSync(true);
                    dbChange = true;
                }
            }
        }
        Map<String, DevFormIndex> indexMap = devFormIndexService.list(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(entity.getId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity(), (a, b) -> a));
        for (DevFormIndex it : entity.getIndexes()) {
            if(it.getId() == null || !indexMap.containsKey(it.getId())){
                it.setIsDbSync(false);
                it.setId(null);
            }else{
                DevFormIndex dbInfo = indexMap.get(it.getId());
                if(!Objects.equals(it.getIndexName(),dbInfo.getIndexName()) ||
                        !Objects.equals(it.getIndexType(),dbInfo.getIndexType()) ||
                        !Objects.equals(it.getColumnNames(),dbInfo.getColumnNames())){
                    it.setIsDbSync(true);
                    dbChange = true;
                }
            }
        }
        entity.setIsDbSync(Constant.YES.equalsIgnoreCase(entity.getIsDbSync()) && !dbChange ? Constant.YES : Constant.NO);
        Assert.isTrue(super.updateById(entity), "保存失败");
        return true;
    }

    private boolean removeDetail(Serializable id) {
        devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(id));
        devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(id));
        return true;
    }

    private void saveDetail(DevForm entity) {
        if (CollUtil.isNotEmpty(entity.getItems())) {
            entity.getItems().forEach(it -> it.setFormId(entity.getId()).setId(null));
            devFormItemService.saveBatch(entity.getItems());
        }
        if (CollUtil.isNotEmpty(entity.getIndexes())) {
            entity.getIndexes().forEach(it -> it.setFormId(entity.getId()).setId(null));
            devFormIndexService.saveBatch(entity.getIndexes());
        }
    }
}
