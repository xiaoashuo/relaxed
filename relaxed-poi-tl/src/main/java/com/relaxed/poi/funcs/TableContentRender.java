package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Tables;
import com.relaxed.poi.domain.TableContentData;
import com.relaxed.poi.enums.ContentTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * A content render implementation for handling table content in Word documents. This
 * renderer processes table data including headers and content rows, and converts them
 * into a format suitable for Word document generation.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class TableContentRender implements WordContentRender<TableContentData> {

	/**
	 * Returns the content type identifier for tables.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.TABLE.name();
	}

	/**
	 * Renders the table content data into a format suitable for Word document generation.
	 * Combines the header and content rows into a single table structure.
	 * @param configure the configuration for rendering
	 * @param data the table content data to render
	 * @return a Tables object containing the rendered table data
	 */
	@Override
	public Object render(Configure configure, TableContentData data) {
		RowRenderData header = data.getHeader();
		List<RowRenderData> contentData = new ArrayList<>();
		contentData.add(header);
		contentData.addAll(data.getContents());
		return Tables.create(contentData.toArray(new RowRenderData[0]));
	}

}
