package com.relaxed.common.model.domain;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 *
 * @author Yakir
 */
@Data
public class PageResult<T> {

	/**
	 * 当前页
	 */
	protected Long current = 1L;

	/**
	 * 总数
	 */
	protected Long total = 0L;

	/**
	 * 查询数据列表
	 */
	protected List<T> records = Collections.emptyList();

	public PageResult() {
	}

	public PageResult(long total) {
		this.total = total;
	}

	public PageResult(List<T> records, long total) {
		this.records = records;
		this.total = total;
	}

	public PageResult(long current, long total, List<T> records) {
		this.current = current;
		this.total = total;
		this.records = records;
	}

}
