package com.relaxed.common.exception.handler;

import com.relaxed.common.exception.ExceptionHandleConfig;

import com.relaxed.common.exception.holder.ExceptionNotifierHolder;

public class DefaultGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	public DefaultGlobalExceptionHandler(ExceptionHandleConfig config, ExceptionNotifierHolder notifierHolder,
			String applicationName) {
		super(config, notifierHolder, applicationName);
	}

}
