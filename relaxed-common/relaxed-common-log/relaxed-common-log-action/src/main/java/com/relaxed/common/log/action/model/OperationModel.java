package com.relaxed.common.log.action.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 操作Model
 *
 * @author yakir
 * @date 2021/12/14 13:52
 */
@Data
public class OperationModel {

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
	 * 旧对象
	 */
	private Object oldValue;

	/**
	 * 新对象
	 */
	private Object newValue;

}
