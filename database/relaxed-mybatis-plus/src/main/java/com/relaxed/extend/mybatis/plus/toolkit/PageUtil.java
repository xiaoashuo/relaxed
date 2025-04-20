package com.relaxed.extend.mybatis.plus.toolkit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.relaxed.common.model.domain.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 * <p>
 * 提供了将通用分页参数转换为 MyBatis-Plus 分页对象的工具方法。 支持动态排序功能，可以根据传入的排序字段和排序方向生成对应的排序条件。
 *
 * @author Hccake
 */
public final class PageUtil {

	/**
	 * 私有构造方法，防止实例化
	 */
	private PageUtil() {
	}

	/**
	 * 根据 PageParam 生成一个 MyBatis-Plus 的 IPage 实例
	 * <p>
	 * 该方法将通用的分页参数对象转换为 MyBatis-Plus 的分页对象， 同时处理排序信息。排序信息从 PageParam 的 sort 字段中获取， 其中 key
	 * 为排序字段名，value 为排序方向（true 表示升序，false 表示降序）
	 * @param pageParam 分页参数对象，包含页码、每页大小、排序信息等
	 * @param <V> 分页记录的类型
	 * @return 返回 MyBatis-Plus 的分页对象，包含分页和排序信息
	 */
	public static <V> IPage<V> prodPage(PageParam pageParam) {
		Page<V> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
		Map<String, Boolean> sortFieldMap = pageParam.getSort();
		sortFieldMap.forEach((field, value) -> {
			OrderItem orderItem = value ? OrderItem.asc(field) : OrderItem.desc(field);
			page.addOrder(orderItem);
		});
		return page;
	}

}
