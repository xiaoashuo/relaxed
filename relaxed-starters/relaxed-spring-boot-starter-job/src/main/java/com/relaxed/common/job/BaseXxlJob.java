package com.relaxed.common.job;

import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yakir
 * @Topic BaseXxlJob
 * @Description
 * @date 2023/9/16 11:15
 * @Version 1.0
 */
@Slf4j
public class BaseXxlJob {

	protected void info(String appendLogPattern, Object... appendLogArguments) {
		log.info(appendLogPattern, appendLogArguments);
		this.printXxlJobLog(appendLogPattern, appendLogArguments);
	}

	protected void error(String appendLogPattern, Object... appendLogArguments) {
		log.error(appendLogPattern, appendLogArguments);
		this.printXxlJobLog(appendLogPattern, appendLogArguments);
	}

	protected void printXxlJobLog(String appendLogPattern, Object... appendLogArguments) {
		XxlJobLogger.log(appendLogPattern, appendLogArguments);
	}

}
