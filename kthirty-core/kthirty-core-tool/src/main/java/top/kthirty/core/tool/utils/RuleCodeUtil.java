package top.kthirty.core.tool.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.cache.Cache;

/**
 * <p>
 * 编码规则工具
 * </p>
 *
 * @author KThirty
 * @since 2023/11/29
 */
@Slf4j
@AllArgsConstructor
public class RuleCodeUtil {
    private static final String KEY_PREFIX = "rule:code";
    private final String category;
    private Handler handler;

    /**
     * 获取Util
     *
     * @param category 规则分类，建议使用{表名:字段名}
     * @param handler  规则处理器
     * @see HandlerPool
     */
    public static RuleCodeUtil getInstance(Handler handler, String category) {
        Assert.notNull(handler, "处理器不可为空");
        Assert.notBlank(category, "分类不可为空");
        return new RuleCodeUtil(category, handler);
    }

    /**
     * 获取下一个
     *
     * @param prefix 前缀，树型结构传入父代码(无上下级管理可空)
     */
    public String next(String prefix) {
        prefix = StrUtil.blankToDefault(prefix, StringPool.EMPTY);
        String key = StrUtil.join(StringPool.COLON, KEY_PREFIX, category, prefix);
        String next = handler.next(Cache.get(key));
        Cache.set(key, next);
        return prefix + next;
    }

    public String next() {
        return next(null);
    }

    /**
     * 比较
     * a > b => 1
     * a = b => 0
     * a < b => -1
     */
    public int compare(String a,String b){
        return handler.compare(a,b);
    }

    /**
     * 记录序列号
     */
    public void record(String code){
        String prefix = null;
        // 截取前缀
        if(handler.getClass() == HandlerPool.SINGLE_LETTER.getClass()){
            prefix = StrUtil.sub(code, 0, code.length() - 3);
        }
        prefix = StrUtil.blankToDefault(prefix, StringPool.EMPTY);
        // 获取当前
        String key = StrUtil.join(StringPool.COLON, KEY_PREFIX, category, prefix);
        String current = Cache.get(key);
        String paramSeq = StrUtil.sub(code, code.length()-3, code.length());
        if(handler.compare(paramSeq,current) == 1){
            Cache.set(key,paramSeq);
            log.debug("更新缓存中的RuleCode {}===>{}",key,paramSeq);
        }
    }

    public interface HandlerPool {
        /**
         * A00-Z99
         */
        Handler SINGLE_LETTER = new SingleLetterSeqHandler();
        /**
         * 0000-9999
         */
        Handler NUMBER_4 = new NumberSeqHandler(4);
    }

    /**
     * 处理器
     */
    public interface Handler {
        String next(String currentVal);

        /**
         * 比较
         * a > b => 1
         * a = b => 0
         * a < b => -1
         */
        int compare(String a,String b);
    }

    /**
     * 单字母序列生成器
     * A00-Z99
     */
    public static class SingleLetterSeqHandler implements Handler {
        @Override
        public String next(String currentVal) {
            if (StrUtil.isBlank(currentVal)) {
                return "A00";
            }
            Assert.isTrue(currentVal.length() == 3, "当前值位数必须为3,但是目前值为{}", currentVal);
            String seq = StrUtil.sub(currentVal, 1, 3);
            char letter = currentVal.charAt(0);
            Assert.isTrue(CharUtil.isLetterUpper(letter), "首位应为A-Z，实为{}", letter);
            Assert.isTrue(NumberUtil.isInteger(seq), "序列值不正确，后两位应为数字，实为{}", seq);
            int nextSeq = NumberUtil.parseInt(seq) + 1;
            if (nextSeq > 99) {
                char nextLetter = (char) (letter + 1);
                Assert.isTrue(CharUtil.isLetterUpper(nextLetter), "序列已用尽");
                return nextLetter + "01";
            } else {
                return letter + StrUtil.fill(StrUtil.toString(nextSeq), '0', 2, true);
            }
        }

        @Override
        public int compare(String a, String b) {
            Assert.isTrue(a.length() == 3);
            Assert.isTrue(b.length() == 3);
            if(a.charAt(0) > b.charAt(0)){
                return 1;
            }else if(a.charAt(0) == a.charAt(0)){
                return NumberUtil.compare(Convert.toInt(a.substring(1)) , Convert.toInt(b.substring(1)));
            }else {
                return -1;
            }
        }
    }

    /**
     * 数字序列号处理器
     */
    @AllArgsConstructor
    public static class NumberSeqHandler implements Handler {
        private int length;

        @Override
        public String next(String currentVal) {
            if (StrUtil.isBlank(currentVal)) {
                return StrUtil.fill("1", '0', length, true);
            }
            Assert.isTrue(NumberUtil.isInteger(currentVal), "当前值不正确，应为数字，实为{}", currentVal);
            String nextVal = StrUtil.toString(NumberUtil.parseInt(currentVal) + 1);
            return StrUtil.fill(nextVal, '0', length, true);
        }

        @Override
        public int compare(String a, String b) {
            return NumberUtil.compare(Convert.toInt(a),Convert.toInt(b));
        }
    }

}
