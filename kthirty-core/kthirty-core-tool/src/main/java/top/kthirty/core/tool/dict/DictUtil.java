package top.kthirty.core.tool.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.List;
import java.util.stream.Collectors;

public class DictUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(DictUtil.class);
    private static final DictProvider DICT_PROVIDER = SpringUtil.getBeanSafe(DictProvider.class);

    /**
     * 获取字典选项列表
     * @param code 字典code
     * @return 选项
     */
    public static List<DictItem> get(String code){
        if(Func.isBlank(code) || Func.isNull(DICT_PROVIDER)){
            return null;
        }
        return DICT_PROVIDER.get(code);
    }

    /**
     * 获取标签
     * @param code 字典代码
     * @param value 字典值
     * @param splitBy 分割符号(多个值分割后获取)
     * @return 标签
     */
    public static String getLabel(String code,String value,String splitBy){
        if(Func.isAnyBlank(code,value)){
            return StringPool.EMPTY;
        }
        List<DictItem> items = get(code);
        if(Func.isEmpty(items)){
            return StringPool.EMPTY;
        }
        // 非多个值
        if(Func.isNull(splitBy)){
            return items.stream().filter(i -> Func.equalsSafe(i.getValue(),value)).map(DictItem::getLabel).collect(Collectors.joining());
        }else{
            List<String> valueList = StrUtil.split(value, splitBy);
            return items.stream().filter(i -> CollUtil.contains(valueList,i.getValue())).map(DictItem::getLabel).collect(Collectors.joining(splitBy));
        }
    }
    public static String getLabel(String code,String value){
        return getLabel(code,value,StringPool.COMMA);
    }

    /**
     * 标签解析为值
     * @param code 字典代码
     * @param label 标签
     * @param splitBy 分割符号(多个值分割后获取)
     * @return 字典值
     */
    public static String getValue(String code,String label,String splitBy){
        if(Func.isAnyBlank(code,label)){
            return StringPool.EMPTY;
        }
        List<DictItem> items = get(code);
        if(Func.isEmpty(items)){
            return StringPool.EMPTY;
        }
        // 非多个值
        if(Func.isNull(splitBy)){
            return items.stream().filter(i -> Func.equalsSafe(i.getLabel(),label)).map(DictItem::getValue).collect(Collectors.joining());
        }else{
            List<String> valueList = StrUtil.split(label, splitBy);
            return items.stream().filter(i -> CollUtil.contains(valueList,i.getLabel())).map(DictItem::getValue).collect(Collectors.joining(splitBy));
        }
    }
    public static String getValue(String code,String label){
        return getValue(code,label,StringPool.COMMA);
    }

    /**
     * 添加缓存
     * @param code 字典code
     * @param items 选项
     * @param time 过期时间-秒 (<=0永不过期)
     */
    public static void add(String code,List<DictItem> items,long time){
        if(Func.isBlank(code) || Func.isEmpty(items) || Func.isNull(DICT_PROVIDER)){
            return;
        }
        DICT_PROVIDER.put(code,items,time);
    }
    public static void add(String code,List<DictItem> items){
        add(code,items,0);
    }

    /**
     * 删除缓存
     * @param code 字典code
     * @param value 字典值（为空删除所有）
     */
    public static void remove(String code,String value){
        if(DICT_PROVIDER != null){
            DICT_PROVIDER.removeCache(code,value);
        }
    }
    public static void remove(String code){
        remove(code,null);
    }
}
