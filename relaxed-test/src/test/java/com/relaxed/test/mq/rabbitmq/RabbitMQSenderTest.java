package com.relaxed.test.mq.rabbitmq;

import cn.hutool.core.collection.ListUtil;
import com.relaxed.autoconfigure.mq.core.IMQSender;
import com.relaxed.autoconfigure.mq.core.annotation.EnableMQ;
import com.relaxed.autoconfigure.mq.core.domain.MQMeta;
import com.relaxed.autoconfigure.mq.core.enums.MQTypeEnum;

import com.relaxed.test.mq.rabbitmq.domain.direct.DirectMQ;
import com.relaxed.autoconfigure.mq.utils.MQHelper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Yakir
 * @Topic RabbitMQSenderTest
 * @Description 1.创建MQ消息
 * @date 2021/12/23 16:12
 * @Version 1.0
 */
@ComponentScan
@EnableMQ(mqType = MQTypeEnum.RABBIT_MQ, basePackages = "com.relaxed.autoconfigure.mq.rabbitmq.domain.direct")
@SpringBootApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("rabbitmq")
class RabbitMQSenderTest {

	@Autowired
	private IMQSender mqSender;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void test() {

		AnnotationMetadata introspect = AnnotationMetadata.introspect(EnableMQ.class);
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes
				.fromMap(introspect.getAnnotationAttributes(EnableMQ.class.getName()));

		System.out.println(mapperScanAttrs);
	}

	@Test
	public void testRabbbitMQ() throws InterruptedException {
		ArrayList<Long> longs = ListUtil.toList(1L);
		// 1.测试直接交换机
		DirectMQ build = DirectMQ.build(longs);
		// 2.测试广播交换机
		// FanoutMQ build = FanoutMQ.build(longs);
		// 3.测试延迟交换机
		// DelayMQ build = DelayMQ.build(longs);
		mqSender.send(build);
		MQMeta mqMeta = MQHelper.getMQMeta(DirectMQ.EXCHANGE_NAME, DirectMQ.QUEUE_NAME, DirectMQ.ROUTE_KEY);
		String exchangeName = mqMeta.getExchangeMeta().getExchangeName();
		Map<String, MQMeta> stringMQMetaMap = MQHelper.globalMQMeta();
		System.out.println(stringMQMetaMap);
		// mqSender.send(build, 5);

		Thread.sleep(500000);
	}

}
