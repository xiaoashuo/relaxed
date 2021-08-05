package com.relaxed.common.model.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数
 *
 * @author Yakir
 */
@Data
public class PageParam {

	/**
	 * 当前页
	 */
	private long current = 1;

	/**
	 * 每页显示条数，默认 10
	 */
	private long size = 10;

	private List<Sort> sorts = new ArrayList<>();

	@Getter
	@Setter
	public static class Sort {

		/**
		 * 排序字段
		 */
		private String field;

		/**
		 * 是否正序排序
		 */
		private boolean asc;

	}

}
