package top.kthirty.core.tool.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import top.kthirty.core.tool.redis.RedisUtil;

/**
 * <p>
 * 编码规则工具
 * <p color="red">强依赖Redis</p>
 * </p>
 *
 * @author KThirty
 * @since 2023/11/29
 */
@AllArgsConstructor
public class RuleCodeUtil {
    private final RedisUtil redisUtil;
    private final String category;
    private Handler handler;

    /**
     * 获取Util
     * @param category 规则分类，建议使用{表名:字段名}
     * @param handler 规则处理器
     * @see HandlerPool
     */
    public static RuleCodeUtil getInstance(Handler handler,String category){
        Assert.notNull(handler,"处理器不可为空");
        Assert.notBlank(category,"分类不可为空");
        return new RuleCodeUtil(SpringUtil.getBean(RedisUtil.class),category,handler);
    }

    /**
     * 获取下一个
     * @param prefix 前缀，树型结构传入父代码(无上下级管理可空)
     */
    public String next(String prefix){
        prefix = StrUtil.blankToDefault(prefix,StringPool.EMPTY);
        String key = StrUtil.join(StringPool.COLON, category, prefix);
        String next = handler.next(redisUtil.get(key));
        redisUtil.set(key,next);
        return prefix + next;
    }
    public String next(){
        return next(null);
    }

    public interface HandlerPool{
        /**
         * A01-Z99
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
    public interface Handler{
        String next(String currentVal);
    }

    /**
     * 单字母序列生成器
     * A01-Z99
     */
    public static class SingleLetterSeqHandler implements Handler{
        @Override
        public String next(String currentVal) {
            if(StrUtil.isBlank(currentVal)){
                return "A01";
            }
            Assert.isTrue(currentVal.length() == 3,"当前值位数必须为3,但是目前值为{}",currentVal);
            String seq = StrUtil.sub(currentVal, 1, 3);
            char letter = currentVal.charAt(0);
            Assert.isTrue(CharUtil.isLetterUpper(letter),"首位应为A-Z，实为{}",letter);
            Assert.isTrue(NumberUtil.isInteger(seq),"序列值不正确，后两位应为数字，实为{}",seq);
            int nextSeq = NumberUtil.parseInt(seq) + 1;
            if(nextSeq > 99){
                char nextLetter = (char)(letter + 1);
                Assert.isTrue(CharUtil.isLetterUpper(nextLetter),"序列已用尽");
                return nextLetter + "01";
            }else{
                return letter + StrUtil.fill(StrUtil.toString(nextSeq),'0',2,true);
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
            if(StrUtil.isBlank(currentVal)){
                return StrUtil.fill("1",'0',length,true);
            }
            Assert.isTrue(NumberUtil.isInteger(currentVal),"当前值不正确，应为数字，实为{}",currentVal);
            String nextVal = StrUtil.toString(NumberUtil.parseInt(currentVal) + 1);
            return StrUtil.fill(nextVal,'0',length,true);
        }
    }

}
