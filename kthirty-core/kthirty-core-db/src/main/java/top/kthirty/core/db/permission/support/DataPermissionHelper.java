package top.kthirty.core.db.permission.support;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.db.permission.DataPermissionContext;
import top.kthirty.core.db.permission.DataPermissionHolder;
import top.kthirty.core.tool.cache.Cache;
import top.kthirty.core.tool.support.Kv;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KThirty
 * @description 数据权限工具类
 * @since 2024/6/13 16:04
 */
@Slf4j
public class DataPermissionHelper {
    private static final String KEY = "sys:dataPermission";
    private static final Map<String, String> HANDLE_RULE_POOL = new HashMap<>();

    static {
        // 模版引擎
        Engine.setFastMode(true);
        Engine.setChineseExpression(true);
        Engine.use().setDevMode(true);
    }

    public synchronized static void add(DataPermission dataPermission) {
        if (dataPermission.getCode() == null || dataPermission.getCode().isBlank()) {
            dataPermission.setCode(IdUtil.fastSimpleUUID());
        }
        if (!Cache.has(KEY)) {
            Cache.set(KEY, MapUtil.newHashMap(true));
        }
        Map<String, DataPermission> map = Cache.get(KEY);
        map.put(dataPermission.getCode(), dataPermission);
        Cache.set(KEY, map);
    }

    public static Collection<DataPermission> getAll() {
        if (!Cache.has(KEY)) {
            return ListUtil.empty();
        }
        Map<String, DataPermission> map = Cache.get(KEY);
        return map.values();
    }

    /**
     * 获取预设规则
     *
     * @param name 规则名
     * @return 规则脚本
     */
    public static String getHandleRule(String name) {
        return HandleRule.getScript(name);
    }

    public static void execScript(String script, Kv param) {
        if(StrUtil.isBlank(script)){
            return;
        }
        log.debug("执行 \n{} \n{}",script,param);
        Template template = Engine.use().getTemplateByString(script);
        String res = template.renderToString(param);
        log.debug("执行结果 {} \n context:{}",res, DataPermissionHolder.getContext());
    }

}
