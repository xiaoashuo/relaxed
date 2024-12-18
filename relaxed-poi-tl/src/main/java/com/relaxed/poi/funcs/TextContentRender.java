package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;

import com.relaxed.poi.domain.TextContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author Yakir
 * @Topic TextContentRender
 * @Description
 * @date 2024/3/25 14:17
 * @Version 1.0
 */
public class TextContentRender implements WordContentRender<TextContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.TEXT.name();
	}

	@Override
	public Object render(Configure configure, TextContentData data) {
		return ObjectUtils.isNotEmpty(data.getLinkData()) ? data.getLinkData()
				: ObjectUtils.isNotEmpty(data.getRenderData()) ? data.getRenderData() : data.getContent();
	}

}
