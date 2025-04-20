package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.ddr.poi.html.HtmlRenderConfig;

/**
 * HTML内容数据类，用于存储 Word 文档中的 HTML 内容数据。 支持 HTML 文本内容和自定义渲染配置，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class HtmlContentData extends LabelData {

	/**
	 * HTML 文本内容，包含 HTML 标签和文本
	 */
	private String content;

	/**
	 * HTML 渲染配置，用于自定义 HTML 内容的渲染方式
	 */
	private HtmlRenderConfig htmlRenderConfig;

}
