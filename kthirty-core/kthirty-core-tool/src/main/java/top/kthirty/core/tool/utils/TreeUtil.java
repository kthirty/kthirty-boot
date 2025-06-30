package top.kthirty.core.tool.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import top.kthirty.core.tool.dict.DictFiller;
import top.kthirty.core.tool.support.Constant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 树Util
 * </p>
 *
 * @author KThirty
 * @since 2024/5/27
 */
public class TreeUtil extends cn.hutool.core.lang.tree.TreeUtil {
    private static final List<String> IGNORE_PROPERTIES = ListUtil.list(true, "id", "parentId", "weight", "name");

    public static Map<String, Object> getExtra(Object entity) {
        Map<String, Object> map = new HashMap<>();
        if (entity != null) {
            // base info
            BeanUtil.toMap(entity).forEach((k, v) -> {
                if (!IGNORE_PROPERTIES.contains(k)) {
                    map.put(k, v);
                }
            });
            // dict
            if (entity instanceof DictFiller) {
                map.putAll(((DictFiller) entity).__dictJsonFiller());
            }
        }
        return map;
    }

    public static <T> List<T> forest(List<T> list) {
        return forest(list, TreeNodeConfig.DEFAULT_CONFIG);
    }

    /**
     * 构建森林节点
     *
     * @param list 原始List
     * @return 森林树
     */
    public static <T> List<T> forest(List<T> list, TreeNodeConfig config) {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 计算森林顶级节点ID
        Set<String> parentIds = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getParentIdKey()),Constant.ROOT_ID)).collect(Collectors.toSet());
        Set<String> ids = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getIdKey()))).collect(Collectors.toSet());
        parentIds.removeAll(ids);
        Map<String, List<T>> idRecordMap = list.stream().collect(Collectors.groupingBy(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getIdKey()))));
        List<T> result = new ArrayList<>();

        parentIds.forEach(parentId -> {
            parentId = Convert.toStr(parentId,Constant.ROOT_ID);
            List<T> children = getChildren(list, parentId, config);
            if(CollUtil.isNotEmpty(children)){
                result.addAll(children);
            }
        });
        return CollUtil.sort(result, Comparator.comparingDouble(it -> Convert.toDouble(ReflectUtil.getFieldValue(it, config.getWeightKey()), 0D)));
    }

    public static <T> List<T> buildBean(List<T> list){
        return buildBean(list, TreeNodeConfig.DEFAULT_CONFIG);
    }
    public static <T> List<T> buildBean(List<T> list,TreeNodeConfig treeNodeConfig) {
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return getChildren(list,  Constant.ROOT_ID,treeNodeConfig);
    }
    public static <T> List<T> getChildren(List<T> list, String parentId){
        return getChildren(list, parentId, TreeNodeConfig.DEFAULT_CONFIG);
    }

    public static <T> List<T> getChildren(List<T> list, String parentId,TreeNodeConfig treeNodeConfig) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        List<T> childrenList = list.stream()
                .filter(it -> {
                    String thisParentId = StrUtil.blankToDefault(Convert.toStr(ReflectUtil.getFieldValue(it, treeNodeConfig.getParentIdKey())), Constant.ROOT_ID);
                    return ObjUtil.equals(thisParentId, parentId);
                })
                .peek(it -> {
                    String thisId = Convert.toStr(ReflectUtil.getFieldValue(it, treeNodeConfig.getIdKey()));
                    List<T> children = getChildren(list, thisId, treeNodeConfig);
                    if (CollUtil.isNotEmpty(children)) {
                        ReflectUtil.setFieldValue(it, treeNodeConfig.getChildrenKey(), children);
                    } else {
                        ReflectUtil.setFieldValue(it, treeNodeConfig.getChildrenKey(), null);
                    }
                })
                .sorted(Comparator.comparingDouble(it -> Convert.toDouble(ReflectUtil.getFieldValue(it,treeNodeConfig.getWeightKey()),0D)))
                .toList();
       return CollUtil.isEmpty(childrenList) ? null : childrenList;
    }
}
