package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;

/**
 * 异常通知器接口
 * <p>
 * 定义了异常通知的统一接口，用于实现不同渠道的异常通知功能。 实现类需要提供具体的通知逻辑，如邮件、钉钉、微信等。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface ExceptionNotifier {

	/**
	 * 获取通知器渠道类型
	 * <p>
	 * 返回通知器的具体实现类型，如MAIL、DING_TALK、WECHAT等。
	 * </p>
	 * @return 通知器渠道类型
	 */
	String getChannel();

	/**
	 * 发送异常通知
	 * <p>
	 * 将异常信息通过指定的渠道发送出去。
	 * </p>
	 * @param sendMessage 需要发送的异常消息
	 * @return 通知发送结果
	 */
	ExceptionNoticeResult send(ExceptionMessage sendMessage);

}
