package com.relaxed.test.mq.rabbitmq.domain.direct;

import com.relaxed.autoconfigure.mq.core.IMQMessageReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic DirectMQReceiver
 * @Description
 * @date 2021/12/27 11:26
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(DirectMQ.IMQReceiver.class)
public class DirectMQReceiver implements IMQMessageReceiver {

	private final DirectMQ.IMQReceiver imqReceiver;

	@Override
	@RabbitListener(queues = DirectMQ.QUEUE_NAME)
	public void receiveMsg(String msg) {
		imqReceiver.receive(DirectMQ.parse(msg));
	}

}
