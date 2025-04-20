package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.NumberingRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.relaxed.poi.domain.ListContentData;
import com.relaxed.poi.enums.ContentTypeEnum;

/**
 * A content render implementation for handling list content in Word documents. This
 * renderer processes list data and converts it into a numbered list format suitable for
 * Word document generation.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class ListContentRender implements WordContentRender<ListContentData> {

	/**
	 * Returns the content type identifier for lists.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.LIST.name();
	}

	/**
	 * Renders the list content data into a numbered list format.
	 * @param configure the configuration for rendering
	 * @param data the list content data to render
	 * @return a NumberingRenderData object containing the rendered list data
	 */
	@Override
	public Object render(Configure configure, ListContentData data) {
		return new NumberingRenderData(data.getNumberingFormat(), data.getList().toArray(new TextRenderData[0]));
	}

}
