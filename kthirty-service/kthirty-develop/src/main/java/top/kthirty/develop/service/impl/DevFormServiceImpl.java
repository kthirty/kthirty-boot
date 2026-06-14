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
        saveItems(entity);
        saveIndexes(entity);
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
        List<DevFormItem> items = CollUtil.emptyIfNull(entity.getItems());
        List<DevFormIndex> indexes = CollUtil.emptyIfNull(entity.getIndexes());

        List<String> keepItemIds = items.stream().map(IdEntity::getId).filter(Objects::nonNull).toList();
        if (CollUtil.isNotEmpty(keepItemIds)) {
            dbChange = devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(entity.getId())
                    .and(DevFormItemTableDef.DEV_FORM_ITEM.ID.notIn(keepItemIds))) || dbChange;
        } else {
            dbChange = devFormItemService.remove(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(entity.getId())) || dbChange;
        }

        List<String> keepIndexIds = indexes.stream().map(IdEntity::getId).filter(Objects::nonNull).toList();
        if (CollUtil.isNotEmpty(keepIndexIds)) {
            dbChange = devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(entity.getId())
                    .and(DevFormIndexTableDef.DEV_FORM_INDEX.ID.notIn(keepIndexIds))) || dbChange;
        } else {
            dbChange = devFormIndexService.remove(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(entity.getId())) || dbChange;
        }

        Map<String, DevFormItem> itemMap = devFormItemService.list(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(entity.getId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity(), (a, b) -> a));
        for (DevFormItem it : items) {
            it.setFormId(entity.getId());
            if (it.getId() == null || !itemMap.containsKey(it.getId())) {
                it.setIsDbSync(false);
                it.setId(null);
                devFormItemService.save(it);
                dbChange = true;
            } else {
                DevFormItem dbInfo = itemMap.get(it.getId());
                if (!Objects.equals(it.getColumnName(), dbInfo.getColumnName())
                        || !Objects.equals(it.getColumnLength(), dbInfo.getColumnLength())
                        || !Objects.equals(it.getColumnPointLength(), dbInfo.getColumnPointLength())
                        || !Objects.equals(it.getColumnType(), dbInfo.getColumnType())) {
                    it.setIsDbSync(false);
                    dbChange = true;
                } else {
                    it.setIsDbSync(dbInfo.getIsDbSync());
                }
                devFormItemService.updateById(it);
            }
        }

        Map<String, DevFormIndex> indexMap = devFormIndexService.list(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(entity.getId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity(), (a, b) -> a));
        for (DevFormIndex it : indexes) {
            it.setFormId(entity.getId());
            if (it.getId() == null || !indexMap.containsKey(it.getId())) {
                it.setIsDbSync(false);
                it.setId(null);
                devFormIndexService.save(it);
                dbChange = true;
            } else {
                DevFormIndex dbInfo = indexMap.get(it.getId());
                if (!Objects.equals(it.getIndexName(), dbInfo.getIndexName())
                        || !Objects.equals(it.getIndexType(), dbInfo.getIndexType())
                        || !Objects.equals(it.getColumnNames(), dbInfo.getColumnNames())) {
                    it.setIsDbSync(false);
                    dbChange = true;
                } else {
                    it.setIsDbSync(dbInfo.getIsDbSync());
                }
                devFormIndexService.updateById(it);
            }
        }

        entity.setIsDbSync(Constant.YES.equalsIgnoreCase(entity.getIsDbSync()) && !dbChange ? Constant.YES : Constant.NO);
        Assert.isTrue(super.updateById(entity), "保存失败");
        return true;
    }

    private void saveItems(DevForm entity) {
        if (CollUtil.isNotEmpty(entity.getItems())) {
            entity.getItems().forEach(it ->
                    it.setFormId(entity.getId())
                            .setIsDbSync(false)
                            .setId(null));
            devFormItemService.saveBatch(entity.getItems());
        }
    }

    private void saveIndexes(DevForm entity) {
        if (CollUtil.isNotEmpty(entity.getIndexes())) {
            entity.getIndexes().forEach(it ->
                    it.setFormId(entity.getId())
                            .setIsDbSync(false)
                            .setId(null));
            devFormIndexService.saveBatch(entity.getIndexes());
        }
    }
}
