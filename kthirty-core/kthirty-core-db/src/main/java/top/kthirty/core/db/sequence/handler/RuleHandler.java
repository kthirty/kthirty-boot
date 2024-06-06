package top.kthirty.core.db.sequence.handler;

/**
 * 处理器
 */
public interface RuleHandler {
    void init(RuleContext context,String[] params);
    /**
     * 获取下一个code
     */
    String next();
    /**
     * 获取当前code
     */
    String current();
    /**
     * 比较
     * a > b => 1
     * a = b => 0
     * a < b => -1
     */
    int compare(String a,String b);

    /**
     * 是否为本规则的合理CODE
     */
    boolean isEffective(String code);

    /**
     * 记录本次code
     * @param code 加入到缓存中
     */
    void record(String code);
}
