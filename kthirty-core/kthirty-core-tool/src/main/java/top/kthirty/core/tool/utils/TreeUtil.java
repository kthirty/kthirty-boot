package top.kthirty.core.tool.utils;

import cn.hutool.core.collection.ListUtil;
import top.kthirty.core.tool.dict.DictFiller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 树Util
 * </p>
 *
 * @author KThirty
 * @since 2024/5/27
 */
public class TreeUtil extends cn.hutool.core.lang.tree.TreeUtil {
    private static final List<String> IGNORE_PROPERTIES = ListUtil.list(true,"id","parentId","weight","name");
    public static Map<String,Object> getExtra(Object entity){
        Map<String,Object> map = new HashMap<>();
        if(entity != null){
            // base info
            BeanUtil.toMap(entity).forEach((k,v) -> {
                if(!IGNORE_PROPERTIES.contains(k)){
                    map.put(k,v);
                }
            });
            // dict
            if(entity instanceof DictFiller){
                map.putAll(((DictFiller)entity).jsonAnyGetter());
            }
        }
        return map;
    }
}
