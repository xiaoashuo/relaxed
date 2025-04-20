package com.relaxed.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页查询结果封装类
 * <p>
 * 用于封装分页查询的结果数据，包含数据列表和总记录数。 当没有数据时，数据列表默认为空列表，总记录数默认为0。
 * </p>
 *
 * @param <T> 数据记录的类型
 * @author Yakir
 * @since 1.0.0
 */
@Data
@Schema(title = "分页返回结果")
public class PageResult<T> {

	/**
	 * 分页数据列表
	 * <p>
	 * 当前页的数据记录列表，如果没有数据则返回空列表。
	 * </p>
	 */
	@Schema(title = "分页数据", description = "当前页的数据记录列表")
	protected List<T> records = Collections.emptyList();

	/**
	 * 总记录数
	 * <p>
	 * 符合查询条件的所有记录总数。
	 * </p>
	 */
	@Schema(title = "数据总量", description = "符合查询条件的所有记录总数")
	protected Long total = 0L;

	/**
	 * 默认构造函数
	 * <p>
	 * 创建一个空的分页结果对象，数据列表为空列表，总记录数为0。
	 * </p>
	 */
	public PageResult() {
	}

	/**
	 * 构造函数
	 * <p>
	 * 创建一个指定总记录数的分页结果对象，数据列表为空列表。
	 * </p>
	 * @param total 总记录数
	 */
	public PageResult(long total) {
		this.total = total;
	}

	/**
	 * 构造函数
	 * <p>
	 * 创建一个包含指定数据列表和总记录数的分页结果对象。
	 * </p>
	 * @param records 数据记录列表
	 * @param total 总记录数
	 */
	public PageResult(List<T> records, long total) {
		this.records = records;
		this.total = total;
	}

}
