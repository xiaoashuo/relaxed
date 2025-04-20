package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;

import com.relaxed.poi.domain.PicContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * A content render implementation for handling picture content in Word documents. This
 * renderer processes picture data from either URLs or local files and supports custom
 * width and height configuration.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class PicContentRender implements WordContentRender<PicContentData> {

	/**
	 * Returns the content type identifier for pictures.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.PICTURE.name();
	}

	/**
	 * Renders the picture content data into a format suitable for Word document
	 * generation. Supports both URL-based and local file-based picture sources.
	 * @param configure the configuration for rendering
	 * @param data the picture content data to render
	 * @return a PictureRenderData object containing the rendered picture data
	 */
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
