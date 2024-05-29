package top.kthirty.core.db.support;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.constant.SqlOperator;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.query.SqlOperators;
import com.mybatisflex.core.util.ClassUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.StringPool;

import java.util.Arrays;
import java.util.Map;

/**
 * 分页工具
 *
 * @author Kthirty
 */
public class Condition {
    public static final int DEFAULT_CURRENT = 1;
    public static final int DEFAULT_SIZE = 10;

    /**
     * 转化成mybatis plus中的Page
     *
     * @param query 查询条件
     * @return IPage
     */
    public static <T> Page<T> getPage(Query query) {
        return new Page<>(ObjUtil.defaultIfNull(query.getPageNumber(), DEFAULT_CURRENT), ObjUtil.defaultIfNull(query.getPageSize(), DEFAULT_SIZE));
    }

    /**
     * 获取mybatis flex中的QueryWrapper
     *
     * @param entity 实体
     * @return QueryWrapper
     */
    public static QueryWrapper getWrapper(Object entity) {
        SqlOperators sqlOperators = SqlOperators.of();
        Arrays.stream(ReflectUtil.getFields(ClassUtil.getUsefulClass(entity.getClass()))).forEach(field -> {
            if (field.getType() != String.class) {
                return;
            }
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            if (StrUtil.isBlank(Convert.toStr(fieldValue))) {
                return;
            }
            String str = Convert.toStr(fieldValue);
            if (StrUtil.startWith(str, StringPool.ASTERISK) && StrUtil.endWith(str, StringPool.ASTERISK)) {
                str = StrUtil.removeSuffix(StrUtil.removePrefix(str, StringPool.ASTERISK), StringPool.ASTERISK);
                ReflectUtil.setFieldValue(entity, field, str);
                sqlOperators.put(field.getName(), SqlOperator.LIKE);
            } else if (StrUtil.startWith(str, StringPool.ASTERISK)) {
                str = StrUtil.removePrefix(str, StringPool.ASTERISK);
                ReflectUtil.setFieldValue(entity, field, str);
                sqlOperators.put(field.getName(), SqlOperator.LIKE_RIGHT);
            } else if (StrUtil.endWith(str, StringPool.ASTERISK)) {
                str = StrUtil.removeSuffix(str, StringPool.ASTERISK);
                ReflectUtil.setFieldValue(entity, field, str);
                sqlOperators.put(field.getName(), SqlOperator.LIKE_LEFT);
            } else {
                sqlOperators.put(field.getName(), SqlOperator.EQUALS);
            }
        });
        return QueryWrapper.create(entity, sqlOperators);
    }

    /**
     * 获取mybatis plus中的QueryWrapper
     *
     * @param query   查询条件
     * @param exclude 排除的查询条件
     * @return QueryWrapper
     */
    public static QueryWrapper buildQuery(Map<String, String> query, String... exclude) {
        if (Func.isNotEmpty(exclude)) {
            Arrays.stream(exclude).forEach(query::remove);
        }
        QueryWrapper qw = new QueryWrapper();
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

}
