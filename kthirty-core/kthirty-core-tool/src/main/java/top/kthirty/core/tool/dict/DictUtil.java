package top.kthirty.core.tool.dict;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.List;
import java.util.stream.Collectors;

public class DictUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(DictUtil.class);
    private static final DictProvider DICT_PROVIDER = SpringUtil.getBeanSafe(DictProvider.class);
    /**
     * 解析字典
     * @param code 字典code
     * @param value 值
     * @param splitBy 多个值的分割符号
     * @return label
     */
    public static String getLabel(String code,String value,String splitBy){
        if(!StrUtil.isAllNotBlank(value,code) || DICT_PROVIDER == null){
            return StringPool.EMPTY;
        }
        List<String> values = StrUtil.isNotBlank(splitBy) ? StrUtil.splitTrim(value,splitBy) : List.of(value);
        // 存在两个 : 且分割后为三个 , 说明为合法的table格式
        if(StrUtil.count(code,StringPool.COLON) == 2 && StrUtil.splitTrim(code, StringPool.COLON).size() == 3){
            List<String> split = StrUtil.split(code, StringPool.COLON);
            String tableName = split.get(0);
            String valueField = split.get(1);
            String labelField = split.get(2);
            return values.stream().map(i -> DICT_PROVIDER.getLabel(tableName,valueField,labelField,i)).collect(Collectors.joining(StringPool.COMMA));
        }else{
            return values.stream().map(i -> DICT_PROVIDER.getLabel(code,i)).collect(Collectors.joining(StringPool.COMMA));
        }
    }
    public static String getLabel(String code,String value){
        return getLabel(code,value,StringPool.COMMA);
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
