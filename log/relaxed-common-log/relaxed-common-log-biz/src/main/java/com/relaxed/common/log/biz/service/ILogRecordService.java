package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.LogBizInfo;

/**
 * 日志记录服务接口，用于持久化业务操作日志 该接口定义了日志记录的基本规范，实现类需要提供具体的日志存储逻辑 支持将业务操作日志保存到数据库、文件系统或其他存储介质
 *
 * @author Yakir
 */
public interface ILogRecordService {

	/**
	 * 保存业务操作日志 将完整的业务操作日志信息持久化到存储介质
	 * @param logBizInfo 业务日志信息对象，包含操作的所有相关信息 包括操作类型、执行结果、性能指标、差异比较等
	 */
	void record(LogBizInfo logBizInfo);

}
