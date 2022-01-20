package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;

/**
 * @author Yakir
 * @Topic ExceptionNotifier
 * @Description
 * @date 2022/1/20 10:38
 * @Version 1.0
 */
public interface ExceptionNotifier {

	/**
	 * 通知器渠道类型
	 * @author yakir
	 * @date 2022/1/20 10:48
	 * @return java.lang.String
	 */
	String getChannel();

	/**
	 * 发送异常通知
	 * @author yakir
	 * @date 2022/1/20 10:44
	 * @param sendMessage
	 * @return com.relaxed.common.exception.domain.ExceptionNoticeResult
	 */
	ExceptionNoticeResult send(ExceptionMessage sendMessage);

}
