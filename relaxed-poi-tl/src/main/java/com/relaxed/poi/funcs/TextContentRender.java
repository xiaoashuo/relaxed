package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

import com.relaxed.poi.domain.TextContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.apache.commons.lang3.ObjectUtils;

/**
 * A content render implementation for handling text content in Word documents. This
 * renderer processes text data and supports different types of text content including
 * plain text, linked text, and custom rendered text.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class TextContentRender implements WordContentRender<TextContentData> {

	/**
	 * Returns the content type identifier for text.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.TEXT.name();
	}

	/**
	 * Renders the text content data into a format suitable for Word document generation.
	 * Prioritizes linked text over rendered text, and rendered text over plain text.
	 * @param configure the configuration for rendering
	 * @param data the text content data to render
	 * @return the rendered text content in the appropriate format
	 */
	@Override
	public Object render(Configure configure, TextContentData data) {
		return ObjectUtils.isNotEmpty(data.getLinkData()) ? data.getLinkData()
				: ObjectUtils.isNotEmpty(data.getRenderData()) ? data.getRenderData() : data.getContent();
	}

}
