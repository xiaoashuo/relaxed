package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.domain.LoopRowTableContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import com.relaxed.poi.plugins.LoopRowTableRenderByHeaderPolicy;

import java.util.List;

/**
 * A content render implementation for handling loop row table content in Word documents.
 * This renderer processes table data that requires row iteration and supports custom
 * prefix/suffix and data start row configuration.
 *
 * @author Yakir
 * @since 1.0.0
 */
public class LoopRowTableContentRender implements WordContentRender<LoopRowTableContentData> {

	/**
	 * Returns the content type identifier for loop row tables.
	 * @return the content type as defined in ContentTypeEnum
	 */
	@Override
	public String contentType() {
		return ContentTypeEnum.LOOP_ROW_TABLE.name();
	}

	/**
	 * Renders the loop row table content data and configures the appropriate render
	 * policy. Injects a custom policy for handling row iteration with the specified
	 * configuration.
	 * @param configure the configuration for rendering
	 * @param data the loop row table content data to render
	 * @return the data list to be processed by the render policy
	 */
	@Override
	public Object render(Configure configure, LoopRowTableContentData data) {
		List dataList = data.getDataList();
		// Inject the plugin
		LoopRowTableRenderByHeaderPolicy policy = new LoopRowTableRenderByHeaderPolicy(data.getPrefix(),
				data.getSuffix(), data.getDataStartRowNum());
		configure.customPolicy(data.getLabelName(), policy);
		return dataList;
	}

}
