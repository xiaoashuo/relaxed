package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

import com.relaxed.poi.domain.HtmlContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.ddr.poi.html.HtmlRenderConfig;
import org.ddr.poi.html.HtmlRenderPolicy;

/**
 * A content render implementation for handling HTML content in Word documents. This
 * renderer processes HTML content and converts it into a format suitable for Word
 * document generation, with support for custom HTML rendering configuration.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class HtmlContentRender implements WordContentRender<HtmlContentData> {

	/**
	 * Returns the content type identifier for HTML content.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.HTML.name();
	}

	/**
	 * Renders the HTML content data and configures the appropriate HTML render policy.
	 * Injects a custom policy for handling HTML content with the specified configuration.
	 * @param configure the configuration for rendering
	 * @param data the HTML content data to render
	 * @return the HTML content to be processed by the render policy
	 */
	@Override
	public Object render(Configure configure, HtmlContentData data) {
		HtmlRenderConfig htmlRenderConfig = data.getHtmlRenderConfig();
		if (htmlRenderConfig == null) {
			htmlRenderConfig = new HtmlRenderConfig();
		}
		// Inject the plugin
		HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy(htmlRenderConfig);
		configure.customPolicy(data.getLabelName(), htmlRenderPolicy);
		return data.getContent();
	}

}
