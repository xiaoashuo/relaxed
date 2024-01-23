package com.relaxed.autoconfigure.mq.rabbitmq.domain.delay;

import com.relaxed.autoconfigure.mq.core.IMQMessageReceiver;
import com.relaxed.autoconfigure.mq.rabbitmq.domain.direct.DirectMQ;
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
@ConditionalOnBean(DelayRealMQ.IMQReceiver.class)
public class DelayMQReceiver implements IMQMessageReceiver {

	@Override
	@RabbitListener(queues = DelayConstants.PROCESS_QUEUE_NAME)
	public void receiveMsg(String msg) {
		log.info("延迟处理队列接收到消息{}", msg);
	}

}
