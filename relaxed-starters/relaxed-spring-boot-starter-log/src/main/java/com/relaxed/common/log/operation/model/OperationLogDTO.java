package com.relaxed.common.log.operation.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Yakir
 * @Topic OperationLog
 * @Description
 * @date 2021/6/27 12:21
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class OperationLogDTO {

	/**
	 * 跟踪ID
	 */
	private String traceId;

	/**
	 * 操作类型
	 */
	private Integer type;

	/**
	 * 日志消息组
	 */
	private String group;

	/**
	 * 日志消息
	 */
	private String msg;

	/**
	 * 请求方式
	 */
	private String method;

	/**
	 * 请求URI
	 */
	private String uri;

	/**
	 * 操作IP地址
	 */
	private String ip;

	/**
	 * 用户代理
	 */
	private String userAgent;

	/**
	 * 操作提交的数据
	 */
	private String params;

	/**
	 * 执行时间
	 */
	private Long time;

	/**
	 * 操作状态
	 */
	private Integer status;

	/**
	 * 操作人
	 */
	private String operator;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
