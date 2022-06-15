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
 * 订单消息发送
 *
 *
 */
@Getter
@Accessors(chain = true)
public class WechatSender {

	/**
	 * 请求路径
	 */
	private final String url;

	@SneakyThrows
	public WechatSender(String url) {
		this.url = url;
	}

	/**
	 * 发送消息 根据参数值判断使用哪种发送方式
	 *
	 */
	@SneakyThrows
	public WechatResponse sendMessage(WechatMessage message) {
		return sendNormalMessage(message);

	}

	/**
	 * 未使用 加签 安全设置 直接发送
	 *
	 */
	public WechatResponse sendNormalMessage(WechatMessage message) {
		return request(message);
	}

	/**
	 * 发起消息请求
	 * @param message 消息内容
	 * @return java.lang.String
	 */
	public WechatResponse request(WechatMessage message) {
		return WechatResponse.of(HttpRequest.post(url).body(message.generate()).execute().body());
	}

}
