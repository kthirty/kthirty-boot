package top.kthirty.core.tool.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import top.kthirty.core.tool.dict.DictFiller;
import top.kthirty.core.tool.jackson.JsonUtil;

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
                map.putAll(((DictFiller) entity).jsonAnyGetter());
            }
        }
        return map;
    }

    public static <T> List<Tree<String>> forest(List<T> list) {
        return forest(list, TreeNodeConfig.DEFAULT_CONFIG);
    }

    /**
     * 构建森林节点
     *
     * @param list 原始List
     * @return 森林树
     */
    public static <T> List<Tree<String>> forest(List<T> list, TreeNodeConfig config) {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 计算森林顶级节点ID
        Set<String> parentIds = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getParentIdKey()))).collect(Collectors.toSet());
        Set<String> ids = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getIdKey()))).collect(Collectors.toSet());
        parentIds.removeAll(ids);
        StaticLog.info("计算顶级耗时{}",timer.intervalPretty());
        // 构建TreeNode
        List<TreeNode<String>> treeNodes = list.stream().map(it -> {
            Map<String, Object> beanMap = BeanUtil.toMap(it);
            TreeNode<String> node = new TreeNode<>();
            node.setId(MapUtil.getStr(beanMap, config.getIdKey()));
            node.setName(MapUtil.getStr(beanMap, config.getNameKey()));
            node.setParentId(MapUtil.getStr(beanMap, config.getParentIdKey()));
            node.setWeight(MapUtil.getInt(beanMap, config.getWeightKey(), 0));
            Map<String, Object> extra = new HashMap<>();
            beanMap.forEach((key, value) -> {
                if (!StrUtil.equalsAny(key, config.getIdKey(), config.getParentIdKey(), config.getWeightKey(), config.getNameKey(), "deleted")) {
                    extra.put(key, value);
                }
            });
            if (it instanceof DictFiller) {
                TimeInterval timer1 = DateUtil.timer();
                timer1.start();
                extra.putAll(((DictFiller) it).jsonAnyGetter());
                StaticLog.info("jsonAnyGetter耗时{}",timer1.intervalPretty());
            }
            node.setExtra(extra);
            return node;
        }).toList();
        StaticLog.info("构建TreeNode耗时{}",timer.intervalPretty());
        List<Tree<String>> forest = new ArrayList<>();
        for (String rootId : parentIds) {
            forest.addAll(build(treeNodes,rootId));
        }
        StaticLog.info("build耗时{}",timer.intervalPretty());
        return forest;
    }

    public static <T> List<T> buildBean(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return getChildren(list, "0");
    }

    public static <T> List<T> getChildren(List<T> list, String parentId) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.stream()
                .filter(it -> {
                    String thisParentId = Convert.toStr(ReflectUtil.getFieldValue(it, "parentId"));
                    return ObjUtil.equals(thisParentId, parentId);
                })
                .peek(it -> {
                    String thisId = Convert.toStr(ReflectUtil.getFieldValue(it, "id"));
                    List<T> children = getChildren(list, thisId);
                    ReflectUtil.setFieldValue(it, "children", children);
                })
                .toList();
    }
}
