package com.relaxed.extend.wechat.message;

/**
 * @author Yakir
 * @Topic WechatGroupMessage
 * @Description
 * @date 2022/6/15 11:46
 * @Version 1.0
 */
public interface WechatMessage {

	/**
	 * 生成钉钉消息发送参数
	 * @return 钉钉文档要求的 jsonString
	 * @author lingting 2020-06-12 19:56:54
	 */
	String generate();

}
