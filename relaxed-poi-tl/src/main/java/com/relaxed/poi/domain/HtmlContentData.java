package com.relaxed.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.ddr.poi.html.HtmlRenderConfig;

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
public class HtmlContentData extends LabelData {

	/**
	 * 纯文本内容
	 */
	private String content;

	/**
	 * html渲染config
	 */
	private HtmlRenderConfig htmlRenderConfig;

}
