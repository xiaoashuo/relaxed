package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.NumberingRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.relaxed.poi.domain.ListContentData;
import com.relaxed.poi.enums.ContentTypeEnum;

/**
 * @author Yakir
 * @Topic TextContentRender
 * @Description
 * @date 2024/3/25 14:17
 * @Version 1.0
 */
public class ListContentRender implements WordContentRender<ListContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.LIST.name();
	}

	@Override
	public Object render(Configure configure, ListContentData data) {
		return new NumberingRenderData(data.getNumberingFormat(), data.getList().toArray(new TextRenderData[0]));
	}

}
