package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.domain.LoopRowTableContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import com.relaxed.poi.plugins.LoopRowTableRenderByHeaderPolicy;

import java.util.List;

/**
 * @author Yakir
 * @Topic LoopRowTableContentRender
 * @Description
 * @date 2024/3/26 10:47
 * @Version 1.0
 */
public class LoopRowTableContentRender implements WordContentRender<LoopRowTableContentData> {

	@Override
	public String contentType() {
		return ContentTypeEnum.LOOP_ROW_TABLE.name();
	}

	@Override
	public Object render(Configure configure, LoopRowTableContentData data) {
		List dataList = data.getDataList();
		// 注入插件
		LoopRowTableRenderByHeaderPolicy policy = new LoopRowTableRenderByHeaderPolicy(data.getPrefix(),
				data.getSuffix(), data.getDataStartRowNum());
		configure.customPolicy(data.getLabelName(), policy);
		return dataList;
	}

}
