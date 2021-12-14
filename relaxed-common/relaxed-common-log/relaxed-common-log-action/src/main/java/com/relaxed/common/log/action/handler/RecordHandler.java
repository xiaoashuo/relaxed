package com.relaxed.common.log.action.handler;

import com.relaxed.common.log.action.model.OperationModel;
import com.relaxed.common.log.action.model.ReportModel;

/**
 * @author Yakir
 * @Topic RecordHandle
 * @Description 记录处理器 主要负责数据上报 推送等操作
 * @date 2021/12/14 14:28
 * @Version 1.0
 */
public interface RecordHandler {

	/**
	 * 上报数据
	 * @param reportModel
	 */
	void report(ReportModel reportModel);

}
