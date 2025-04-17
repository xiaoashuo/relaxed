package com.relaxed.test.mq.rabbitmq.domain.fanout;

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
@ConditionalOnBean(Fanout2MQ.IMQReceiver.class)
public class FanoutMQ2Receiver implements IMQMessageReceiver {

	@Override
	@RabbitListener(queues = Fanout2MQ.QUEUE_NAME)
	public void receiveMsg(String msg) {
		log.info("广播交换机接收消息2-{}", msg);
	}

}
