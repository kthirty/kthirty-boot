package top.kthirty.core.tool.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.List;

public class DictUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(DictUtil.class);
    private static final DictProvider DICT_PROVIDER = SpringUtil.getBeanSafe(DictProvider.class);

    /**
     * 获取字典选项列表
     *
     * @param code 字典code
     * @return 选项
     */
    public static List<DictItem> get(String code) {
        Assert.hasText(code, "字典代码不可为空");
        Assert.notNull(DICT_PROVIDER, "字典解析器不存在");
        return DICT_PROVIDER.get(code);
    }

    /**
     * 获取标签
     *
     * @param code      字典代码
     * @param value     字典值
     * @param separator 分割符号(多个值分割后获取)
     * @return 标签
     */
    public static String getLabel(String code, String value, String separator) {
        Assert.notNull(DICT_PROVIDER, "字典解析器不存在");
        return DICT_PROVIDER.getLabel(code, value, separator);
    }

    /**
     * 标签解析为值
     *
     * @param code      字典代码
     * @param label     标签
     * @param separator 分割符号(多个值分割后获取)
     * @return 字典值
     */
    public static String getValue(String code, String label, String separator) {
        Assert.notNull(DICT_PROVIDER, "字典解析器不存在");
        return DICT_PROVIDER.getValue(code, label, separator);
    }

    /**
     * 添加缓存
     *
     * @param code  字典code
     * @param items 选项
     * @param time  过期时间-秒 (<=0永不过期)
     */
    public static void add(String code, List<DictItem> items, long time) {
        Assert.hasText(code, "字典代码不可为空");
        Assert.notNull(DICT_PROVIDER, "字典解析器不存在");
        Assert.notEmpty(items, "列表不可为空");
        DICT_PROVIDER.put(code, items, time);
    }

    /**
     * 删除缓存
     *
     * @param code  字典code
     * @param value 字典值（为空删除所有）
     */
    public static void remove(String code, String value) {
        Assert.notNull(DICT_PROVIDER, "字典解析器不存在");
        DICT_PROVIDER.removeCache(code, value);
    }

    public static void add(String code, List<DictItem> items) {
        add(code, items, 0);
    }

    public static void remove(String code) {
        remove(code, null);
    }

    public static String getValue(String code, String label) {
        return getValue(code, label, StringPool.COMMA);
    }

    public static String getLabel(String code, String value) {
        return getLabel(code, value, StringPool.COMMA);
    }
}
