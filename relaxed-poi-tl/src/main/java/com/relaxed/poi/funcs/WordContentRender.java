package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.domain.LabelData;


/**
 * @author Yakir
 * @Topic WordContentRender
 * @Description word 内容渲染接口
 * @date 2024/3/25 14:16
 * @Version 1.0
 */
public interface WordContentRender<T extends LabelData> {

	/**
	 * 数据类型
	 * @return 当前处理器支持类型
	 */
	String contentType();

	/**
	 * 渲染填充数据值
	 * @param configure 当前使用配置对象
	 * @param data
	 * @return 值对象
	 */
	Object render(Configure configure, T data);

}
