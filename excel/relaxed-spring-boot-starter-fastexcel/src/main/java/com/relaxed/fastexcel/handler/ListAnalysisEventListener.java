package com.relaxed.fastexcel.handler;

import cn.idev.excel.event.AnalysisEventListener;
import com.relaxed.fastexcel.vo.ErrorMessage;

import java.util.List;

/**
 * Excel列表解析事件监听器 用于处理Excel数据解析过程中的事件 主要功能: 1. 支持Excel数据解析为对象列表 2. 支持数据校验错误收集 3.
 * 提供统一的数据访问接口
 *
 * @author L.cm
 * @param <T> 解析的数据类型
 * @since 1.0.0
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	/**
	 * 获取Excel解析后的对象列表 用于获取所有成功解析的数据对象
	 * @return 解析后的对象列表
	 */
	public abstract List<T> getList();

	/**
	 * 获取数据校验错误信息列表 用于获取解析过程中的错误信息
	 * @return 错误信息列表
	 */
	public abstract List<ErrorMessage> getErrors();

}
