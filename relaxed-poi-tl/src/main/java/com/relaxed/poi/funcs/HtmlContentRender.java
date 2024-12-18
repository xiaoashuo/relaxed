package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

import com.relaxed.poi.domain.HtmlContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.ddr.poi.html.HtmlRenderConfig;
import org.ddr.poi.html.HtmlRenderPolicy;

/**
 * @author Yakir
 * @Topic TextContentRender
 * @Description
 * @date 2024/3/25 14:17
 * @Version 1.0
 */
public class HtmlContentRender implements WordContentRender<HtmlContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.HTML.name();
	}

	@Override
	public Object render(Configure configure, HtmlContentData data) {
		HtmlRenderConfig htmlRenderConfig = data.getHtmlRenderConfig();
		if (htmlRenderConfig == null) {
			htmlRenderConfig = new HtmlRenderConfig();
		}
		// 注入插件
		HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy(htmlRenderConfig);
		configure.customPolicy(data.getLabelName(), htmlRenderPolicy);
		return data.getContent();

	}

}
