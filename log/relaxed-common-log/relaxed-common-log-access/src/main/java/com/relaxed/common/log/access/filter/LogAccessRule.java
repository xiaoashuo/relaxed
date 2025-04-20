package com.relaxed.common.log.access.filter;

import lombok.Data;

/**
 * 访问日志规则类。 用于配置访问日志的匹配规则和记录选项。 主要功能包括： 1. 配置URL匹配模式 2. 配置日志记录选项 3. 配置字段过滤规则
 *
 * @author Yakir
 * @since 1.0
 */
@Data
public class LogAccessRule {

	/**
	 * URL匹配模式（Ant风格） 用于匹配需要记录访问日志的请求路径
	 */
	private String urlPattern;

	/**
	 * 日志记录选项 用于配置日志记录的具体行为
	 */
	private RecordOption recordOption = new RecordOption();

	/**
	 * 字段过滤规则 用于配置请求和响应字段的过滤规则
	 */
	private FieldFilter fieldFilter = new FieldFilter();

	/**
	 * 字段过滤规则类。 用于配置请求和响应字段的过滤规则。
	 */
	@Data
	public class FieldFilter {

		/**
		 * 请求字段匹配模式 用于匹配需要过滤的请求字段
		 */
		private String matchRequestKey;

		/**
		 * 响应字段匹配模式 用于匹配需要过滤的响应字段
		 */
		private String matchResponseKey;

		/**
		 * 字段替换文本 当字段被过滤时，使用此文本替换原内容 默认为 "long content replace..."
		 */
		private String replaceText = "long content replace...";

	}

	/**
	 * 日志记录选项类。 用于配置日志记录的具体行为。
	 */
	@Data
	public class RecordOption {

		/**
		 * 是否忽略当前规则的日志记录 默认为 false
		 */
		private boolean ignore = false;

		/**
		 * 是否记录请求信息 默认为 true
		 */
		private boolean includeRequest = true;

		/**
		 * 是否记录响应信息 默认为 true
		 */
		private boolean includeResponse = true;

	}

}
