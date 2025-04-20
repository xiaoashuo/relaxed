package com.relaxed.extend.dingtalk.message;

/**
 * 钉钉消息接口。 定义了钉钉机器人消息的基本行为，所有类型的消息都需要实现此接口。
 *
 * @author lingting
 * @since 1.0
 */
public interface DingTalkMessage {

	/**
	 * 生成钉钉消息发送参数。 将消息内容转换为钉钉机器人API要求的JSON字符串格式。
	 * @return 符合钉钉文档要求的JSON字符串
	 */
	String generate();

}
