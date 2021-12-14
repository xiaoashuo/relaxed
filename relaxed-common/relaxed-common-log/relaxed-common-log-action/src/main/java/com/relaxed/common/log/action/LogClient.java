package com.relaxed.common.log.action;

import java.time.LocalDateTime;

import com.relaxed.common.log.action.handler.DataHandler;
import com.relaxed.common.log.action.model.OperationModel;
import com.relaxed.common.log.action.properties.LogClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic LogClient
 * @Description
 * @date 2021/12/14 14:07
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class LogClient {

	private final DataHandler dataHandler;

	private final LogClientProperties logClientProperties;

	public void logObject(String objectId, String operator, String operationName, String operationAlias,
			String extraWords, String comment, Object oldObject, Object newObject) {
		OperationModel operationModel = new OperationModel();
		operationModel.setAppName(logClientProperties.getAppName());
		operationModel.setObjectName(oldObject.getClass().getSimpleName());
		operationModel.setObjectId(objectId);
		operationModel.setOperator(operator);
		operationModel.setOperationName(operationName);
		operationModel.setOperationAlias(operationAlias);
		operationModel.setExtraWords(extraWords);
		operationModel.setComment(comment);
		operationModel.setOldValue(oldObject);
		operationModel.setNewValue(newObject);
		operationModel.setOperationTime(LocalDateTime.now());
		log.info("日志记录开始{}", operationModel);
		// 将比对方法下沉到记录 时间点
		try {
			dataHandler.recordObject(operationModel);
		}
		catch (Exception e) {
			log.error("日志记录异常{}", objectId, e);
		}
	}

}
