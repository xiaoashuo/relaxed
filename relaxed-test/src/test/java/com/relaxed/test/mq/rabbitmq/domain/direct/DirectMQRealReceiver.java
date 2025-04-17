package com.relaxed.test.mq.rabbitmq.domain.direct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic DirectMQRealReceiver
 * @Description
 * @date 2021/12/27 13:38
 * @Version 1.0
 */
@Slf4j
@Component
public class DirectMQRealReceiver implements DirectMQ.IMQReceiver {

	@Override
	public void receive(DirectMQ.MsgPayload payload) {
		log.info("真实接收到消息{}", payload);
	}

}
