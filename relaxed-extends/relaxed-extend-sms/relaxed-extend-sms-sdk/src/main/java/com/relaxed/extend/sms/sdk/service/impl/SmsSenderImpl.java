package com.relaxed.extend.sms.sdk.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.model.result.R;
import com.relaxed.extend.sms.sdk.SmsSender;
import com.relaxed.extend.sms.sdk.core.SmsProperties;
import com.relaxed.extend.sms.sdk.dto.BaseParam;
import com.relaxed.extend.sms.sdk.dto.SmsParam;
import com.relaxed.extend.sms.sdk.dto.SmsResult;
import com.relaxed.extend.sms.sdk.handler.IEncryptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Yakir
 * @Topic SmsSenderImpl
 * @Description
 * @date 2021/8/26 14:52
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class SmsSenderImpl extends AbstractSender {

	private final SmsProperties smsProperties;

	private final IEncryptionHandler iEncryptionHandler;

	private final String send = "/sms/send";

	@Override
	public SmsResult send(SmsParam smsParam) {
		String url = generateUrl(smsProperties.getHost(), send);
		return send(url, smsParam);
	}

	private SmsResult send(String url, SmsParam smsParam) {
		smsParam.setAccessKeyId(smsProperties.getAccessKey());
		if (smsProperties.isAuth()) {
			if (StrUtil.isBlank(smsProperties.getAccessKey()) || StrUtil.isBlank(smsProperties.getAccessSecret())) {
				return SmsResult.fail("accessKey 不能为空");
			}
			smsParam.setTimestamp(System.currentTimeMillis());
			smsParam.setEncryption(iEncryptionHandler.encode(smsParam.getEncryption(), smsParam.getAccessKeyId(),
					smsProperties.getAccessSecret()));
		}
		if (StrUtil.isBlank(url)) {
			return SmsResult.fail("url 不能为空");
		}
		try {
			HttpResponse httpResponse = HttpRequest.post(url).timeout(-1).body(JSONUtil.toJsonStr(smsParam)).execute();
			String body = httpResponse.body();
			int status = httpResponse.getStatus();
			if (httpResponse.isOk()) {
				return JSONUtil.toBean(body, SmsResult.class);
			}
			else {
				log.error("httpRequest access fail ,StatusCode is:{},text: {}", status, body);
				return SmsResult.fail("status is" + status + "res" + body);
			}
		}
		catch (Exception e) {
			log.error("error :", e);
			return SmsResult.fail(e.getMessage());
		}
	}

}
