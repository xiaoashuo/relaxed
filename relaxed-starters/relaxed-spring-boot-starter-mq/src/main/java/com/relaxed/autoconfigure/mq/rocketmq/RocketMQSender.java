package com.relaxed.autoconfigure.mq.rocketmq;

import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消息发送器实现类
 * <p>
 * 实现了IMQSender接口，提供RocketMQ消息发送功能，支持实时发送和延迟发送消息。 根据消息元数据中的主题和标签进行消息发送。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
public class RocketMQSender implements IMQSender {

	private final RocketMQTemplate rocketMQTemplate;

	/**
	 * 获取RocketMQ客户端实例
	 * <p>
	 * 当前实现返回null，需要根据实际需求实现。
	 * </p>
	 * @param <C> 客户端类型
	 * @return RocketMQ客户端实例，当前返回null
	 */
	@Override
	public <C> C client() {
		return null;
	}

	/**
	 * 实时发送消息到RocketMQ
	 * <p>
	 * 根据消息元数据中的主题和标签发送消息。
	 * </p>
	 * @param mqModel 消息模型，包含消息内容和元数据
	 */
	@Override
	public void send(AbstractMQ mqModel) {
		MQMeta mqMeta = mqModel.getMQMeta();
		MQMeta.ExchangeMeta exchangeMeta = mqMeta.getExchangeMeta();
		String topic = exchangeMeta.getExchangeName();
		MQMeta.QueueMeta queueMeta = mqMeta.getQueueMeta();
		String tag = queueMeta.getQueueName();
		// rocketMQTemplate.convertAndSend(topic,tag, mqModel.toMessage());
	}

	/**
	 * 延迟发送消息到RocketMQ
	 * <p>
	 * 根据消息元数据和延迟时间发送消息。
	 * </p>
	 * @param mqModel 消息模型，包含消息内容和元数据
	 * @param delay 延迟时间，单位为秒
	 */
	@Override
	public void send(AbstractMQ mqModel, int delay) {
		// TODO: 实现延迟发送逻辑
	}

}
