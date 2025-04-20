package com.relaxed.extend.wechat.message;

/**
 * 企业微信消息接口。 定义了企业微信消息的基本行为，所有具体的消息类型都需要实现此接口。
 *
 * @author Yakir
 * @since 1.0
 */
public interface WechatMessage {

	/**
	 * 生成企业微信消息发送参数
	 * @return 符合企业微信API要求的JSON字符串
	 */
	String generate();

}
