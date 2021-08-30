package com.relaxed.extend.sms.sdk.service.impl;

import com.relaxed.extend.sms.sdk.SmsSender;

/**
 * @author Yakir
 * @Topic AbstractSender
 * @Description
 * @date 2021/8/26 15:20
 * @Version 1.0
 */
public abstract class AbstractSender implements SmsSender {

	protected String generateUrl(String domain, String path) {
		return domain + path;
	}

}
