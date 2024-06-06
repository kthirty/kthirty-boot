package top.kthirty.core.db.sequence.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import top.kthirty.core.tool.cache.Cache;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.utils.StringPool;

/**
 * 单字母序列生成器
 * <pre>
 * example:
 * A00
 *  A00A01
 *  A00A02
 * A01
 *  A01A01
 *  A01A02
 *  </pre>
 * params: [前缀:sql或者固定前缀]
 * 默认参数为{@link TreeSeqHandler#QUERY_PARENT_CODE}
 */
public class TreeSeqHandler implements RuleHandler {
    public static final String QUERY_PARENT_CODE = "select {column} as prefix from {table} where id = '{parentId}'";
    private String prefix;
    private String category;

    @Override
    public void init(RuleContext context, String[] params) {
        this.category = context.getTable() + ":" + context.getColumn();
        String prefixStr = Convert.toStr(ArrayUtil.get(params, 0),QUERY_PARENT_CODE);
        if (StrUtil.startWithIgnoreCase(prefixStr, "select")) {
            Kv sqlParam = Kv.init().set("table", context.getTable())
                    .set("column", context.getColumn())
                    .setAll(BeanUtil.toMap(context.getEntity()));
            Row row = Db.selectOneBySql(StrUtil.format(prefixStr, sqlParam));
            prefixStr = row != null ? row.getString("prefix") : StringPool.EMPTY;
        }
        this.prefix = prefixStr;
    }

    @Override
    public String next() {
        String nextVal;
        String currentVal = current();
        if (StrUtil.isBlank(currentVal)) {
            nextVal = "A00";
        }else{
            Assert.isTrue(currentVal.length() == 3, "当前值位数必须为3,但是目前值为{}", currentVal);
            String seq = StrUtil.sub(currentVal, 1, 3);
            char letter = currentVal.charAt(0);
            Assert.isTrue(CharUtil.isLetterUpper(letter), "首位应为A-Z，实为{}", letter);
            Assert.isTrue(NumberUtil.isInteger(seq), "序列值不正确，后两位应为数字，实为{}", seq);
            int nextSeq = NumberUtil.parseInt(seq) + 1;
            if (nextSeq > 99) {
                char nextLetter = (char) (letter + 1);
                Assert.isTrue(CharUtil.isLetterUpper(nextLetter), "序列已用尽");
                nextVal = nextLetter + "01";
            } else {
                nextVal = letter + StrUtil.fill(StrUtil.toString(nextSeq), '0', 2, true);
            }
            nextVal = prefix + nextVal;
        }
        record(nextVal);
        return nextVal;
    }

    @Override
    public String current() {
        String finalCode = StrUtil.join(":", ClassUtil.getClassName(this, true)
                , this.category
                , this.prefix);
        return Cache.get(finalCode);
    }

    @Override
    public int compare(String a, String b) {
        if (a == null && b == null) {
            return 1;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        Assert.isTrue(a.length() == 3);
        Assert.isTrue(b.length() == 3);
        if (a.charAt(0) > b.charAt(0)) {
            return 1;
        } else if (a.charAt(0) == a.charAt(0)) {
            return NumberUtil.compare(Convert.toInt(a.substring(1)), Convert.toInt(b.substring(1)));
        } else {
            return -1;
        }
    }

    @Override
    public boolean isEffective(String code) {
        if(StrUtil.isBlank(code)){
            return false;
        }
        for (String str : StrUtil.split(code, 3)) {
            // 开头不是A-Z
            if (!('A' <= str.charAt(0) && str.charAt(0) <= 'Z')) {
                return false;
            }
            // 后两位不是数字
            if (!NumberUtil.isInteger(str.substring(1))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void record(String code) {
        if(isEffective(code)){
            String prefix = code.substring(0, code.length() - 3);
            String currentCode = code.substring(code.length() - 3);
            String finalCode = StrUtil.join(":", ClassUtil.getClassName(this, true)
                    , this.category
                    , prefix);
            String cacheCode = Cache.get(finalCode);
            if(compare(currentCode,cacheCode) == 1){
                Cache.set(finalCode,currentCode);
            }
        }
    }
}
