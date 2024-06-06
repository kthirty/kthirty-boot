package top.kthirty.core.db.sequence.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import top.kthirty.core.tool.cache.Cache;

/**
 * 数字序列号处理器
 */
public class NumberSeqHandler implements RuleHandler {
    private String category;
    private int length;

    @Override
    public void init(RuleContext context,String[] params) {
        this.category = context.getTable() + ":" + context.getColumn();
        this.length = Convert.toInt(ArrayUtil.get(params,0),4);
    }

    @Override
    public String next() {
        String current = current();
        if (StrUtil.isBlank(current) || !NumberUtil.isInteger(current)) {
            return StrUtil.fill("1", '0', length, true);
        }
        String nextVal = StrUtil.toString(NumberUtil.parseInt(current) + 1);
        return StrUtil.fill(nextVal, '0', length, true);
    }

    @Override
    public String current() {
        String finalCode = StrUtil.join(":"
                , ClassUtil.getClassName(this, true)
                , this.category);
        return Cache.get(finalCode);
    }
    @Override
    public int compare(String a, String b) {
        return NumberUtil.compare(Convert.toInt(a),Convert.toInt(b));
    }

    @Override
    public boolean isEffective(String code) {
        if(StrUtil.isBlank(code)){
            return false;
        }
        return NumberUtil.isInteger(code);
    }

    @Override
    public void record(String code) {
        // 大于当前缓存则记录，目的是缓存中始终记录最大的code，已实现后续递增
        if (isEffective(code)) {
            String current = current();
            if(compare(code,current) == 1){
                String finalCode = StrUtil.join(":"
                        , ClassUtil.getClassName(this, true)
                        , this.category);
                Cache.set(finalCode,code);
            }
        }
    }
}
