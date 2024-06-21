package top.kthirty.core.db.sequence.handler;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.row.Db;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;

/**
 * @description 执行sql获取最新的code
 * <p>
 *     参数：查询下一个code的sql，sql应返回一个code字段
 *     eg: select max({column}) code from {table} where deleted = 0
 * </p>
 * @author KThirty
 * @since 2024/6/21 8:26
 */
public class SqlSeqHandler implements RuleHandler {
    private String sql;
    @Override
    public void init(RuleContext context, String[] params) {
        this.sql = StrUtil.format(params[0], Kv.init()
                .setAll(BeanUtil.toMap(context.getEntity()))
                .set("table",context.getTable())
                .set("column",context.getColumn())
        );
    }

    @Override
    public String next() {
        return Db.selectOneBySql(this.sql).getString("code");
    }

    @Override
    public String current() {
        throw new IllegalCallerException("不支持此操作");
    }

    @Override
    public int compare(String a, String b) {
        throw new IllegalCallerException("不支持此操作");
    }

    @Override
    public boolean isEffective(String code) {
        throw new IllegalCallerException("不支持此操作");
    }

    @Override
    public void record(String code) {
        throw new IllegalCallerException("不支持此操作");
    }
}
