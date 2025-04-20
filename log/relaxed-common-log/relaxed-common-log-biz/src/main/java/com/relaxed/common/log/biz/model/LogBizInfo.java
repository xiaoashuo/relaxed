package com.relaxed.common.log.biz.model;

import lombok.Data;
import sun.reflect.generics.tree.ReturnType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务日志信息类，用于封装业务操作日志的详细信息 该类包含了业务操作的基本信息、执行结果、性能指标以及相关的上下文数据 支持记录操作前后的差异比较、函数执行结果和表达式解析结果
 *
 * @author Yakir
 */
@Data
public class LogBizInfo {

	/**
	 * 系统名称，默认从 Spring 应用名称获取 用于标识日志所属的系统或服务
	 */
	private String systemName;

	/**
	 * 模块名称，标识业务操作所属的功能模块 例如：用户管理、订单管理等
	 */
	private String moduleName;

	/**
	 * 跟踪ID，用于关联同一业务操作的所有日志记录 通常用于分布式系统中的请求追踪
	 */
	private String traceId;

	/**
	 * 类路径，记录执行操作的类全限定名
	 */
	private String className;

	/**
	 * 方法名，记录执行操作的具体方法名
	 */
	private String methodName;

	/**
	 * 操作者，记录执行操作的用户或系统标识
	 */
	private String operator;

	/**
	 * 业务编号，用于标识具体的业务实体 例如：订单号、用户ID等
	 */
	private String bizNo;

	/**
	 * 操作类型，描述业务操作的性质 例如：新增、修改、删除等
	 */
	private String type;

	/**
	 * 操作开始时间，时间戳，单位：毫秒
	 */
	private Long startTime;

	/**
	 * 操作结束时间，时间戳，单位：毫秒
	 */
	private Long endTime;

	/**
	 * 操作执行时间，单位：毫秒 计算方法：endTime - startTime
	 */
	private Long executeTime;

	/**
	 * 操作是否成功 true 表示成功，false 表示失败
	 */
	private Boolean success;

	/**
	 * 操作成功时的日志文本 用于记录操作成功时的详细信息
	 */
	private String successText;

	/**
	 * 操作失败时的日志文本 用于记录操作失败时的错误信息
	 */
	private String failText;

	/**
	 * 操作执行后的返回结果，以JSON字符串形式存储
	 */
	private String result;

	/**
	 * 错误信息，记录操作失败时的具体错误描述
	 */
	private String errorMsg;

	/**
	 * 详细说明，记录操作的补充信息
	 */
	private String details;

	/**
	 * 表达式解析结果映射 key: 表达式标识 value: 表达式解析后的结果
	 */
	private Map<String, String> expressionMap = new HashMap<>();

	/**
	 * 函数执行结果映射 key: 函数标识 value: 函数执行后的结果
	 */
	private Map<String, String> funcValMap = new HashMap<>();

	/**
	 * 日志增强数据 用于存储额外的业务数据，支持自定义扩展
	 */
	private Map<String, Object> enhanceData = new HashMap<>();

	/**
	 * 差异比较结果 key: 差异标识 value: 属性变更列表
	 */
	private Map<String, List<AttributeModel>> diffResult = new HashMap<>();

}
