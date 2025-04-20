package com.relaxed.common.core.exception.handler;

public interface GlobalExceptionHandler {

	/**
	 * 在此处理错误信息 进行落库，入ES， 发送报警通知等信息
	 * @param throwable 异常
	 */
	void handle(Throwable throwable);

}
