package com.relaxed.poi.domain;

import com.deepoove.poi.data.HyperlinkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文本内容数据类，用于存储 Word 文档中的文本数据。 支持纯文本、带样式文本和超链接文本，支持链式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TextContentData extends LabelData {

	/**
	 * 纯文本内容，不包含任何样式
	 */
	private String content;

	/**
	 * 带样式的文本数据，包含字体、颜色等样式信息
	 */
	private TextRenderData renderData;

	/**
	 * 超链接文本数据，包含链接地址和显示文本
	 */
	private HyperlinkTextRenderData linkData;

}
