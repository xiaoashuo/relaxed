package com.relaxed.autoconfigure.mq.rabbitmq;

import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQSendTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic RabbitMQSender
 * @Description
 * @date 2021/12/23 16:04
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class RabbitMQSender implements IMQSender {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public <C> C client() {
		return (C) rabbitTemplate;
	}

	@Override
	public void send(AbstractMQ mqModel) {
		send(mqModel, 0);
	}

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
