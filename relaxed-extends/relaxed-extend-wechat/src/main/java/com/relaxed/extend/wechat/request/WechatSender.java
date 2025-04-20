package com.relaxed.extend.wechat.request;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

import com.relaxed.extend.wechat.message.WechatMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 微信消息发送器。 用于向企业微信发送不同类型的消息，支持普通消息发送。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public class WechatSender {

	/**
	 * 请求路径
	 */
	private final String url;

	/**
	 * 构造函数
	 * @param url 企业微信机器人Webhook地址
	 */
	@SneakyThrows
	public WechatSender(String url) {
		this.url = url;
	}

	/**
	 * 发送消息，根据参数值判断使用哪种发送方式
	 * @param message 要发送的消息
	 * @return 发送结果响应
	 */
	@SneakyThrows
	public WechatResponse sendMessage(WechatMessage message) {
		return sendNormalMessage(message);
	}

	/**
	 * 使用普通方式发送消息，不使用加签安全设置
	 * @param message 要发送的消息
	 * @return 发送结果响应
	 */
	public WechatResponse sendNormalMessage(WechatMessage message) {
		return request(message);
	}

	/**
	 * 发起消息请求
	 * @param message 消息内容
	 * @return 发送结果响应
	 */
	public WechatResponse request(WechatMessage message) {
		return WechatResponse.of(HttpRequest.post(url).body(message.generate()).execute().body());
	}

}
