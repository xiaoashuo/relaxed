package com.relaxed.common.log.action.handler;

import com.relaxed.common.log.action.model.OperationModel;

/**
 * @author Yakir
 * @Topic RecordHandler
 * @Description
 * @date 2021/12/14 14:08
 * @Version 1.0
 */
public interface DataHandler {

	/**
	 * 记录对象
	 * @author yakir
	 * @date 2021/12/14 14:25
	 * @param operationModel
	 */
	void recordObject(OperationModel operationModel);

}
