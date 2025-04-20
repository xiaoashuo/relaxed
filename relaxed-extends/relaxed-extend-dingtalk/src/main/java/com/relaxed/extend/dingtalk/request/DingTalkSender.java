package com.relaxed.extend.dingtalk.request;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.relaxed.extend.dingtalk.message.DingTalkMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 钉钉消息发送器。 提供了钉钉机器人消息发送的核心功能，支持普通发送和加签发送两种方式。
 *
 * @author lingting
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public class DingTalkSender {

	/**
	 * 钉钉机器人 Webhook 地址
	 */
	private final String url;

	/**
	 * 钉钉机器人安全设置的签名密钥
	 */
	private String secret;

	/**
	 * 消息签名工具
	 */
	private final Mac mac;

	@SneakyThrows
	public DingTalkSender(String url) {
		this.url = url;
		mac = Mac.getInstance("HmacSHA256");
	}

	/**
	 * 发送消息。 根据是否配置签名密钥，自动选择普通发送或加签发送方式。
	 * @param message 待发送的消息内容
	 * @return 钉钉接口响应对象
	 */
	@SneakyThrows
	public DingTalkResponse sendMessage(DingTalkMessage message) {
		if (StrUtil.isEmpty(secret)) {
			return sendNormalMessage(message);
		}
		else {
			return sendSecretMessage(message);
		}
	}

	/**
	 * 使用普通方式发送消息。 不使用安全设置的签名机制，直接发送消息。
	 * @param message 待发送的消息内容
	 * @return 钉钉接口响应对象
	 */
	public DingTalkResponse sendNormalMessage(DingTalkMessage message) {
		return request(message, false);
	}

	/**
	 * 使用加签方式发送消息。 使用安全设置的签名机制发送消息，需要先配置签名密钥。
	 * @param message 待发送的消息内容
	 * @return 钉钉接口响应对象
	 */
	@SneakyThrows
	public DingTalkResponse sendSecretMessage(DingTalkMessage message) {
		return request(message, true);
	}

	/**
	 * 设置签名密钥。 设置后将启用加签方式发送消息。
	 * @param secret 签名密钥
	 * @return 当前发送器实例
	 */
	@SneakyThrows
	public DingTalkSender setSecret(String secret) {
		if (StrUtil.isNotEmpty(secret)) {
			this.secret = secret;
			mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		}
		return this;
	}

	/**
	 * 生成加签后的请求地址。
	 * @param timestamp 当前时间戳
	 * @return 包含签名信息的完整请求地址
	 */
	@SneakyThrows
	public String secret(long timestamp) {
		return url + "&timestamp=" + timestamp + "&sign=" + URLEncoder.encode(
				Base64.encode(mac.doFinal((timestamp + "\n" + secret).getBytes(StandardCharsets.UTF_8))), "UTF-8");
	}

	/**
	 * 发起消息请求。
	 * @param message 消息内容
	 * @param isSecret 是否使用加签方式，true 表示使用加签
	 * @return 钉钉接口响应对象
	 */
	public DingTalkResponse request(DingTalkMessage message, boolean isSecret) {
		if (isSecret) {
			return DingTalkResponse.of(HttpRequest.post(secret(System.currentTimeMillis()))
					// 请求体
					.body(message.generate())
					// 获取返回值
					.execute().body());
		}
		else {
			return DingTalkResponse.of(HttpRequest.post(url).body(message.generate()).execute().body());
		}
	}

}
