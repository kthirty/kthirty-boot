package top.kthirty.core.db.support;

import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;

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
        return new Page<>(ObjUtil.defaultIfNull(query.getPageNumber(),DEFAULT_CURRENT), ObjUtil.defaultIfNull(query.getPageSize(), DEFAULT_SIZE));
	}

	/**
	 * 获取mybatis flex中的QueryWrapper
	 *
	 * @param entity 实体
	 * @return QueryWrapper
	 */
	public static QueryWrapper getWrapper(Object entity) {
		return QueryWrapper.create(entity);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query   查询条件
	 * @param exclude 排除的查询条件
	 * @return QueryWrapper
	 */
	public static QueryWrapper getQueryWrapper(Map<String, String> query, Map<String, Object> exclude) {
		exclude.forEach((k, v) -> query.remove(k));
		QueryWrapper qw = new QueryWrapper();
		SqlKeyword.buildCondition(query, qw);
		return qw;
	}

}
