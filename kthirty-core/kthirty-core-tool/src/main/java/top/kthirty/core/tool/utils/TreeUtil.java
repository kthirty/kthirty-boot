package top.kthirty.core.tool.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
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
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 计算森林顶级节点ID
        Set<String> parentIds = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getParentIdKey()))).collect(Collectors.toSet());
        Set<String> ids = list.parallelStream().map(it -> Convert.toStr(ReflectUtil.getFieldValue(it, config.getIdKey()))).collect(Collectors.toSet());
        parentIds.removeAll(ids);
        // 构建TreeNode
        List<Tree<String>> forest = new ArrayList<>();
        parentIds.forEach(rootId -> {
            List<Tree<String>> trees = build(list, rootId, config, (object, treeNode) -> {
                if (treeNode.getWeight() == null) {
                    treeNode.setWeight(0);
                }
                Objects.requireNonNull(JsonUtil.toMap(JsonUtil.toJson(object)))
                        .forEach((k, v) -> {
                            if (!treeNode.containsKey(k)) {
                                treeNode.putExtra(k, v);
                            }
                        });
                if (object instanceof DictFiller) {
                    ((DictFiller) object).jsonAnyGetter().forEach(treeNode::putExtra);
                }
            });
            forest.addAll(trees);
        });
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
