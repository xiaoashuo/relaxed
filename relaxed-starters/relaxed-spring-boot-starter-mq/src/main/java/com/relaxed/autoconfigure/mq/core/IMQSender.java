package com.relaxed.autoconfigure.mq.core;

import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;

/**
 * 消息队列发送者接口。 定义消息发送的基本操作，包括实时发送和延迟发送。 实现该接口的类需要提供具体的消息发送逻辑。
 *
 * @author Yakir
 * @since 1.0
 */
public interface IMQSender {

	/**
	 * 获取消息队列客户端实例。 用于获取底层消息队列的客户端对象，以便进行更底层的操作。
	 * @param <C> 客户端类型
	 * @return 消息队列客户端实例
	 */
	<C> C client();

	/**
	 * 实时发送消息。 将消息立即发送到消息队列，不进行延迟。
	 * @param mqModel 消息模型，包含消息内容和相关配置
	 */
	void send(AbstractMQ mqModel);

	/**
	 * 延迟发送消息。 将消息延迟指定时间后发送到消息队列。
	 * @param mqModel 消息模型，包含消息内容和相关配置
	 * @param delay 延迟时间，单位为秒
	 */
	void send(AbstractMQ mqModel, int delay);

}
