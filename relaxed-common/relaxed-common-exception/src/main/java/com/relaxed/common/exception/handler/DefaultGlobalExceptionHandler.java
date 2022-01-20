package com.relaxed.common.exception.handler;

import com.relaxed.common.exception.ExceptionHandleConfig;

import com.relaxed.common.exception.holder.ExceptionNotifierHolder;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:06 默认的异常日志处理类
 */
public class DefaultGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	public DefaultGlobalExceptionHandler(ExceptionHandleConfig config, ExceptionNotifierHolder notifierHolder,
			String applicationName) {
		super(config, notifierHolder, applicationName);
	}

}
