package com.relaxed.poi.domain;

import com.deepoove.poi.data.HyperlinkTextRenderData;
import com.deepoove.poi.data.TextRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic TextContentData
 * @Description
 * @date 2024/3/25 13:55
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TextContentData extends LabelData {

	/**
	 * 纯文本内容
	 */
	private String content;

	/**
	 * 带样式文本
	 */
	private TextRenderData renderData;

	/**
	 * 超链接文本
	 */
	private HyperlinkTextRenderData linkData;

}
