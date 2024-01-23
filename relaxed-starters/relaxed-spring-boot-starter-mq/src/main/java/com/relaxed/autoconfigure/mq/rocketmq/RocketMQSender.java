package com.relaxed.autoconfigure.mq.rocketmq;

import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.domain.AbstractMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic RocketMQSender
 * @Description
 * @date 2021/12/28 9:18
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class RocketMQSender implements IMQSender {

	private final RocketMQTemplate rocketMQTemplate;

	@Override
	public <C> C client() {
		return null;
	}

	@Override
	public void send(AbstractMQ mqModel) {
		MQMeta mqMeta = mqModel.getMQMeta();
		MQMeta.ExchangeMeta exchangeMeta = mqMeta.getExchangeMeta();
		String topic = exchangeMeta.getExchangeName();
		MQMeta.QueueMeta queueMeta = mqMeta.getQueueMeta();
		String tag = queueMeta.getQueueName();
		// rocketMQTemplate.convertAndSend(topic,tag, mqModel.toMessage());
	}

	@Override
	public void send(AbstractMQ mqModel, int delay) {

	}

}
