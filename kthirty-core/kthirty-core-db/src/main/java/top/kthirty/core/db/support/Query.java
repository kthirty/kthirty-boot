package top.kthirty.core.db.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页工具
 *
 * @author Kthirty
 */
@Data
@Accessors(chain = true)
public class Query {

	/**
	 * 当前页
	 */
	private Integer current;

	/**
	 * 每页的数量
	 */
	private Integer size;
}
