package top.kthirty.core.db.support;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import top.kthirty.core.tool.Func;

import java.util.Map;

/**
 * 定义常用的 sql关键字
 *
 * @author Kthirty
 */
public class SqlKeyword {
	private final static String SQL_REGEX = "'|%|--|insert|delete|select|count|group|union|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|sql";

	private static final String EQUAL = "_equal";
	private static final String NOT_EQUAL = "_notequal";
	private static final String LIKE = "_like";
	private static final String NOT_LIKE = "_notlike";
	private static final String GT = "_gt";
	private static final String LT = "_lt";
	private static final String IS_NULL = "_null";
	private static final String NOT_NULL = "_notnull";
	private static final String IGNORE = "_ignore";

	/**
	 * 条件构造器
	 *
	 * @param query 查询字段
	 * @param qw    查询包装类
	 */
	public static void buildCondition(Map<String, String> query, QueryWrapper qw) {
		if (CollUtil.isEmpty(query)) {
			return;
		}
		query.forEach((k, v) -> {
			if (ObjectUtil.hasEmpty(k, v) || k.endsWith(IGNORE)) {
				return;
			}
			k = filter(k);
			v = filter(Func.toStr(v));
			if (k.endsWith(EQUAL)) {
				qw.eq(getColumn(k, EQUAL), v);
			} else if (k.endsWith(NOT_EQUAL)) {
				qw.ne(getColumn(k, NOT_EQUAL), v);
			} else if (k.endsWith(NOT_LIKE)) {
				qw.notLike(getColumn(k, NOT_LIKE), v);
			} else if (k.endsWith(GT)) {
				qw.gt(getColumn(k, GT), v);
			} else if (k.endsWith(LT)) {
				qw.lt(getColumn(k, LT), v);
			} else if (k.endsWith(IS_NULL)) {
				qw.isNull(getColumn(k, IS_NULL));
			} else if (k.endsWith(NOT_NULL)) {
				qw.isNotNull(getColumn(k, NOT_NULL));
			} else {
				qw.like(getColumn(k, LIKE), v);
			}
		});
	}

	/**
	 * 获取数据库字段
	 *
	 * @param column  字段名
	 * @param keyword 关键字
	 * @return String
	 */
	private static String getColumn(String column, String keyword) {
		return StrUtil.toUnderlineCase(StrUtil.removeSuffix(column, keyword));
	}

	/**
	 * 把SQL关键字替换为空字符串
	 *
	 * @param param 关键字
	 * @return string
	 */
	public static String filter(String param) {
		if (param == null) {
			return null;
		}
		return param.replaceAll("(?i)" + SQL_REGEX, StrUtil.EMPTY);
	}

}
