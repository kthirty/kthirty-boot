package top.kthirty.core.db.support;


import com.mybatisflex.core.paginate.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 视图包装基类
 *
 * @author Kthirty
 */
public abstract class BaseEntityWrapper<E, V> {

	/**
	 * 单个实体类包装
	 *
	 * @param entity 实体类
	 * @return V
	 */
	public abstract V entityVO(E entity);

	/**
	 * 实体类集合包装
	 *
	 * @param list 列表
	 * @return List V
	 */
	public List<V> listVO(List<E> list) {
		return list.stream().map(this::entityVO).collect(Collectors.toList());
	}

	/**
	 * 分页实体类集合包装
	 *
	 * @param pages 分页
	 * @return Page V
	 */
	public Page<V> pageVO(Page<E> pages) {
		List<V> records = listVO(pages.getRecords());
		return new Page<>(records,pages.getPageNumber(), pages.getPageSize(), pages.getTotalRow());
	}

}
