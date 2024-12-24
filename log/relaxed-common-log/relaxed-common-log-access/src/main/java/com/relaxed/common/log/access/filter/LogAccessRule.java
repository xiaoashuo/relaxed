package com.relaxed.common.log.access.filter;

import lombok.Data;

/**
 * @author Yakir
 * @Topic LogAccessRule
 * @Description
 * @date 2024/12/24 9:49
 * @Version 1.0
 */
@Data
public class LogAccessRule {

	/**
	 * 当前设置匹配的 url 规则（Ant风格）
	 */
	private String urlPattern;

	/**
	 * 日志记录选项
	 */
	private RecordOption recordOption = new RecordOption();

	/**
	 * 字段过滤
	 */
	private FieldFilter fieldFilter = new FieldFilter();

	@Data
	public class FieldFilter {

		/**
		 * 请求key
		 */
		private String matchRequestKey;

		/**
		 * 响应key
		 */
		private String matchResponseKey;

		/**
		 * 日志替换文本
		 */

		private String replaceText = "long content replace...";

	}

	@Data
	public class RecordOption {

		/**
		 * 忽略当前记录
		 */
		private boolean ignore = false;

		/**
		 * 记录请求
		 */
		private boolean includeRequest = true;

		/**
		 * 记录响应
		 */
		private boolean includeResponse = true;

	}

}
