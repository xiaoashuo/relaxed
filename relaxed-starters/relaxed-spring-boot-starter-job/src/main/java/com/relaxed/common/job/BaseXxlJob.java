package com.relaxed.common.job;

import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

/**
 * @author Yakir
 * @Topic BaseXxlJob
 * @Description
 * @date 2023/9/16 11:15
 * @Version 1.0
 */
public class BaseXxlJob {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static Logger xxlLog = LoggerFactory.getLogger("xxl-job logger");

	protected void info(String appendLogPattern, Object... appendLogArguments) {
		log.info(appendLogPattern, appendLogArguments);
		this.printXxlJobLog(appendLogPattern, appendLogArguments);
	}

	protected void error(String appendLogPattern, Object... appendLogArguments) {
		log.error(appendLogPattern, appendLogArguments);
		this.printXxlJobLog(appendLogPattern, appendLogArguments);
	}

	protected void printXxlJobLog(String appendLogPattern, Object... appendLogArguments) {
		FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
		String appendLog = ft.getMessage();
		StackTraceElement callInfo = (new Throwable()).getStackTrace()[2];
		logDetail(callInfo, appendLog);
	}

	private static void logDetail(StackTraceElement callInfo, String appendLog) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DateUtil.formatDateTime(new Date())).append(" ")
				.append("[" + callInfo.getClassName() + "#" + callInfo.getMethodName() + "]").append("-")
				.append("[" + callInfo.getLineNumber() + "]").append("-")
				.append("[" + Thread.currentThread().getName() + "]").append(" ")
				.append(appendLog != null ? appendLog : "");
		String formatAppendLog = stringBuffer.toString();
		String logFileName = (String) XxlJobFileAppender.contextHolder.get();
		if (logFileName != null && logFileName.trim().length() > 0) {
			XxlJobFileAppender.appendLog(logFileName, formatAppendLog);
		}
		else {
			xxlLog.info(">>>>>>>>>>> {}", formatAppendLog);
		}

	}

}
