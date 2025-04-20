package com.relaxed.poi.enums;

/**
 * 内容类型枚举，定义了 Word 文档中支持的各种内容类型。
 *
 * @author Yakir
 * @since 1.0.0
 */
public enum ContentTypeEnum {

	/**
	 * 文本内容类型，用于纯文本内容的渲染
	 */
	TEXT,

	/**
	 * 图片内容类型，用于图片内容的渲染
	 */
	PICTURE,

	/**
	 * 表格内容类型，用于表格内容的渲染
	 */
	TABLE,

	/**
	 * 列表内容类型，用于列表内容的渲染
	 */
	LIST,

	/**
	 * 图表内容类型，用于图表内容的渲染
	 */
	CHART,

	/**
	 * 循环行表格内容类型，用于动态生成多行表格
	 */
	LOOP_ROW_TABLE,

	/**
	 * 附件内容类型，用于附件内容的渲染
	 */
	ATTACHMENT,

	/**
	 * HTML内容类型，用于HTML内容的渲染
	 */
	HTML;

}
