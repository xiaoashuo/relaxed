package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.LogBizInfo;

/**
 * @author Yakir
 * @Topic ILogRecordService
 * @Description
 * @date 2023/12/14 11:31
 * @Version 1.0
 */
public interface ILogRecordService {

	/**
	 * 保存 log
	 * @param logBizInfo 日志实体
	 */
	void record(LogBizInfo logBizInfo);

}
