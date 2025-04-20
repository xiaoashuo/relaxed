package com.relaxed.autoconfigure.mq.rabbitmq;

import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ消息发送器实现类
 * <p>
 * 实现了IMQSender接口，提供RabbitMQ消息发送功能，支持普通队列、广播和延迟消息发送。
 * </p>
 *
 * @author Yakir
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class RabbitMQSender implements IMQSender {

	private final RabbitTemplate rabbitTemplate;

	/**
	 * 获取RabbitMQ客户端实例
	 * @return RabbitTemplate实例
	 */
	@Override
	public <C> C client() {
		return (C) rabbitTemplate;
	}

	/**
	 * 发送消息到RabbitMQ
	 * @param mqModel 消息模型
	 */
	@Override
	public void send(AbstractMQ mqModel) {
		send(mqModel, 0);
	}

	/**
	 * 发送消息到RabbitMQ，支持延迟发送
	 * @param mqModel 消息模型
	 * @param delay 延迟时间（秒）
	 */
	@Override
	public void send(AbstractMQ mqModel, int delay) {
		MQMeta mqMeta = mqModel.getMQMeta();
		MQMeta.ExchangeMeta exchangeMeta = mqMeta.getExchangeMeta();

		MQMeta.QueueMeta queueMeta = mqMeta.getQueueMeta();
		MQSendTypeEnum exchangeType = exchangeMeta.getExchangeType();
		switch (exchangeType) {
		case QUEUE:
			this.rabbitTemplate.convertAndSend(exchangeMeta.getExchangeName(), mqMeta.getRoutingKey(),
					mqModel.toMessage());
			break;
		case BROADCAST:
			// fanout模式 的 routeKEY 没意义。 没有延迟属性
			this.rabbitTemplate.convertAndSend(exchangeMeta.getExchangeName(), null, mqModel.toMessage());
			break;
		case DELAY:
			rabbitTemplate.convertAndSend(queueMeta.getQueueName(), mqModel.toMessage(), messagePostProcessor -> {
				messagePostProcessor.getMessageProperties()
						.setExpiration(String.valueOf(Math.toIntExact(delay * 1000)));
				return messagePostProcessor;
			});
			break;
		}

	}

}
