package com.relaxed.common.log.operation.service.impl;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic DefaultLogRecordHandler
 * @Description
 * @date 2023/12/18 14:32
 * @Version 1.0
 */
@Slf4j
public class DefaultLogRecordService implements ILogRecordService {

	@Override
	public void record(LogBizInfo logBizInfo) {
		log.info("[触发默认业务日志记录]=====>log={}", JSONUtil.toJsonStr(logBizInfo));
	}

}
