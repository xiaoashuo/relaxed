package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;

import com.relaxed.poi.domain.PicContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Yakir
 * @Topic PicContentRender
 * @Description
 * @date 2024/3/25 15:43
 * @Version 1.0
 */
public class PicContentRender implements WordContentRender<PicContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.PICTURE.name();
	}

	@Override
	public Object render(Configure configure, PicContentData data) {
		PictureRenderData renderData;
		if (StringUtils.isNotBlank(data.getPicUrl())) {
			renderData = Pictures.ofUrl(data.getPicUrl()).size(data.getWidth(), data.getHeight()).create();
		}
		else {
			renderData = new PictureRenderData(data.getWidth(), data.getHeight(), data.getLocalFile());
		}
		return renderData;
	}

}
