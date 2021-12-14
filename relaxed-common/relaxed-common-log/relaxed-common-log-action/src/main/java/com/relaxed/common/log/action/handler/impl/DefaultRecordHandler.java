package com.relaxed.common.log.action.handler.impl;

import com.relaxed.common.log.action.handler.RecordHandler;
import com.relaxed.common.log.action.model.OperationModel;
import com.relaxed.common.log.action.model.ReportModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic DefaultRecordHandler
 * @Description
 * @date 2021/12/14 14:28
 * @Version 1.0
 */
@Slf4j
public class DefaultRecordHandler implements RecordHandler {

	@Override
	public void report(ReportModel reportModel) {
		log.info("接收到上报数据{}", reportModel);
	}

}
