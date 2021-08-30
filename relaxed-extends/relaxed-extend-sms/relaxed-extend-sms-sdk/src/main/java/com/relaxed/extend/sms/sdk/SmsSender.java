package com.relaxed.extend.sms.sdk;

import com.relaxed.common.model.result.R;
import com.relaxed.extend.sms.sdk.dto.SmsParam;
import com.relaxed.extend.sms.sdk.dto.SmsResult;

/**
 * @author Yakir
 * @Topic SmsSender
 * @Description 短信发送器
 * @date 2021/8/3 17:42
 * @Version 1.0
 */
public interface SmsSender {

	/**
	 * 发送单条短信
	 * @author yakir
	 * @date 2021/8/3 18:04
	 * @param smsParam
	 * @return R
	 */
	SmsResult send(SmsParam smsParam);

}
