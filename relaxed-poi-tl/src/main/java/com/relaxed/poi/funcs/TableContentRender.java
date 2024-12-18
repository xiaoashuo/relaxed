package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Tables;
import com.relaxed.poi.domain.TableContentData;
import com.relaxed.poi.enums.ContentTypeEnum;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic TextContentRender
 * @Description
 * @date 2024/3/25 14:17
 * @Version 1.0
 */
public class TableContentRender implements WordContentRender<TableContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.TABLE.name();
	}

	@Override
	public Object render(Configure configure, TableContentData data) {
		RowRenderData header = data.getHeader();
		List<RowRenderData> contentData = new ArrayList<>();
		contentData.add(header);
		contentData.addAll(data.getContents());
		return Tables.create(contentData.toArray(new RowRenderData[0]));
	}

}
