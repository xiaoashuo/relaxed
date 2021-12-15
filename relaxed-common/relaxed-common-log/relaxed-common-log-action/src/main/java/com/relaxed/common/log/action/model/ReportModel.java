package com.relaxed.common.log.action.model;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic ReportModel
 * @Description 上报数据
 * @date 2021/12/14 14:59
 * @Version 1.0
 */
@Data
public class ReportModel {

	/**
	 * id
	 */
	private Integer id;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 对象名称
	 */
	private String objectName;

	/**
	 * traceId
	 */
	private String tractId;

	/**
	 * 对象关联id
	 */
	private String objectId;

	/**
	 * 操作者
	 */
	private String operator;

	/**
	 * 操作名称
	 */
	private String operationName;

	/**
	 * 操作别名
	 */
	private String operationAlias;

	/**
	 * 附件描述
	 */
	private String extraWords;

	/**
	 * 描述
	 */
	private String comment;

	/**
	 * 操作时间
	 */
	private LocalDateTime operationTime;

	/**
	 * 旧值json
	 */
	private String oldValue;

	/**
	 * 新值json
	 */
	private String newValue;

	/**
	 * 属性列表
	 */
	private List<AttributeModel> attributeModelList = new ArrayList<AttributeModel>();

	public static ReportModel of(OperationModel operationModel) {
		ReportModel reportModel = new ReportModel();
		reportModel.setAppName(operationModel.getAppName());
		reportModel.setTractId(operationModel.getTractId());
		reportModel.setObjectName(operationModel.getObjectName());
		reportModel.setObjectId(operationModel.getObjectId());
		reportModel.setOperator(operationModel.getOperator());
		reportModel.setOperationName(operationModel.getOperationName());
		reportModel.setOperationAlias(operationModel.getOperationAlias());
		reportModel.setExtraWords(operationModel.getExtraWords());
		reportModel.setComment(operationModel.getComment());
		reportModel.setOperationTime(operationModel.getOperationTime());
		reportModel.setOldValue(JSONUtil.toJsonStr(operationModel.getOldValue()));
		reportModel.setNewValue(JSONUtil.toJsonStr(operationModel.getNewValue()));
		return reportModel;
	}

}
