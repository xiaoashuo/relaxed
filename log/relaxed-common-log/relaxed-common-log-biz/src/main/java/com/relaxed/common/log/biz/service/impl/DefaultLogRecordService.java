package com.relaxed.common.log.biz.service.impl;

import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认日志记录服务实现类 该实现类提供了日志记录的默认实现 主要功能包括： 1. 将日志业务信息转换为 JSON 格式并记录到日志文件 2.
 * 提供默认的日志记录实现，方便业务方按需扩展 3. 支持通过重写 record 方法实现自定义的日志记录逻辑
 *
 * @author Yakir
 */
@Slf4j
public class DefaultLogRecordService implements ILogRecordService {

	/**
	 * 记录日志业务信息 默认实现将日志信息转换为 JSON 格式并输出到日志文件 业务方可以根据需要重写此方法，实现自定义的日志记录逻辑 例如： 1. 将日志保存到数据库
	 * 2. 发送到消息队列 3. 写入到其他存储系统
	 * @param logBizInfo 日志业务信息
	 */
	@Override
	public void record(LogBizInfo logBizInfo) {
		log.info("[触发默认业务日志记录]=====>log={}", JSONUtil.toJsonStr(logBizInfo));
	}

}
